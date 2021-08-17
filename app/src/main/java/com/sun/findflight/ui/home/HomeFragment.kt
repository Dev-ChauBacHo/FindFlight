package com.sun.findflight.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Place
import com.sun.findflight.databinding.FragmentHomeBinding
import com.sun.findflight.ui.addpassenger.AddPassengerFragment
import com.sun.findflight.ui.basicflightslist.BasicFlightsListFragment
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import com.sun.findflight.ui.searchPlace.SearchPlaceFragment
import com.sun.findflight.utils.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener, HomeContract.View {

    private val containerContext by lazy { context as MainActivity }
    private var adult = DEFAULT_ADULT_SEAT
    private var child = NO_PASSENGER
    private var infant = NO_PASSENGER
    private var placeFromObject: Place? = null
    private var placeToObject: Place? = null
    private var presenter: HomeContract.Presenter? = null
    private var localBasicFlight: BasicFlight? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    override fun initComponents() {
        //TODO change fragment_home in main project
        containerContext.changeTitle(getString(R.string.app_name))
    }

    override fun initEvents() {
        viewBinding.run {
            listOf(
                textSearchPlaceFrom, textSearchPlaceTo, textSearchDateFrom, textSearchDateTo,
                textSearchReturnDate, textChangePassenger, imageSwapPlaces, imageCancelPlaceTo,
                imageCancelDateFrom, imageCancelDateTo, imageCancelReturnDate,
                imageDropdownTravelClass, imageDropdownCurrency, buttonFindFlight
            ).forEach { it.setOnClickListener(this@HomeFragment) }
        }

        setPlaceListener(KEY_PLACE_FROM, viewBinding.textSearchPlaceFrom)
        setPlaceListener(KEY_PLACE_TO, viewBinding.textSearchPlaceTo)
        setPassengerListener(viewBinding.textChangePassenger)
    }

    override fun initData() {
        val currency = resources.getStringArray(R.array.currency_list)
        viewBinding.spinnerCurrency.adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, currency) }

        val travelClass = resources.getStringArray(R.array.travel_class)
        viewBinding.spinnerTravelClass.adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, travelClass) }

        context?.let {
            val repository = RepositoryUtils.getBasicFlightRepository(it)
            presenter = HomePresenter(this, repository)
        }
        presenter?.getBasicFlight()
    }

    override fun onClick(v: View?): Unit = with(viewBinding) {
        when (v) {
            textSearchPlaceFrom -> {
                textPlaceFrom.setTextColor(resources.getColor(R.color.color_pickled_bluewood, null))
                parentFragmentManager.addFragment(
                    R.id.frameMain,
                    SearchPlaceFragment(),
                    KEY_PLACE_FROM
                )
            }
            textSearchPlaceTo -> parentFragmentManager.addFragment(
                R.id.frameMain,
                SearchPlaceFragment(),
                KEY_PLACE_TO
            )
            textSearchDateFrom -> parentFragmentManager.chooseDate(
                textSearchDateFrom,
                resources.getString(R.string.text_choose_date)
            )
            textSearchDateTo -> {
                if (textSearchDateFrom.text.isEmpty()) {
                    context?.showToast(getString(R.string.text_choose_date_from_first))
                } else {
                    parentFragmentManager.chooseDate(
                        textSearchDateTo,
                        resources.getString(R.string.text_choose_date)
                    )
                }
            }
            textSearchReturnDate -> parentFragmentManager.chooseDate(
                textSearchReturnDate,
                resources.getString(R.string.text_choose_return_date)
            )
            textChangePassenger -> {
                val fragment = AddPassengerFragment()
                val bundle = Bundle()
                bundle.putString(KEY_ADULT, adult)
                bundle.putString(KEY_CHILD, child)
                bundle.putString(KEY_INFANT, infant)
                fragment.arguments = bundle
                fragment.show(parentFragmentManager, null)
            }
            imageSwapPlaces -> swapTextViewContent(textSearchPlaceFrom, textSearchPlaceTo)
            imageCancelPlaceTo -> {
                placeToObject = null
                textSearchPlaceTo.text = ""
                localBasicFlight?.destinationName = null
                localBasicFlight?.destinationCode = null
            }
            imageCancelDateFrom -> {
                textSearchDateFrom.text = ""
                textSearchDateTo.text = ""
                localBasicFlight?.departureDate = null
            }
            imageCancelDateTo -> {
                textSearchDateTo.text = ""
                val date = localBasicFlight?.departureDate
                date?.indexOf(",")?.let {
                    localBasicFlight?.departureDate = date.removeRange(it, date.length)
                }
            }
            imageCancelReturnDate -> {
                textSearchReturnDate.text = ""
                localBasicFlight?.returnDate = null
            }
            imageDropdownTravelClass -> spinnerTravelClass.performClick()
            imageDropdownCurrency -> spinnerCurrency.performClick()
            //TODO add option to not choose any travel class
            buttonFindFlight -> findFlight()
            else -> Unit
        }
    }

    override fun showBasicFlight(basicFlight: BasicFlight) = with(viewBinding) {
        localBasicFlight = basicFlight
        basicFlight.run {
            textSearchPlaceFrom.text = originName
            textSearchPlaceTo.text = destinationName
            switchOneway.isChecked = oneWay
            departureDate?.let {
                val dates = it.split(",")
                textSearchDateFrom.text = dates[0]
                if (dates.size > 1) textSearchDateTo.text = dates[1]
            }
            textSearchReturnDate.text = returnDate
            this@HomeFragment.adult = adult
            this@HomeFragment.child = child
            this@HomeFragment.infant = infant
            textChangePassenger.text = getPassengerString()

            val travel = resources.getStringArray(R.array.travel_class)
            val currency = resources.getStringArray(R.array.currency_list)
            for (i in currency.indices) {
                if (currency[i].split(" ").first().equals(currencyCode, true)) {
                    spinnerCurrency.setSelection(i)
                    break
                }
            }
            spinnerTravelClass.setSelection(travel.indexOf(travelClass))
        }
    }

    private fun setPlaceListener(requestKey: String, textView: TextView) {
        parentFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == requestKey) {
                val result = bundle.getParcelable<Place>(KEY_DATA)
                if (requestKey == KEY_PLACE_FROM) {
                    placeFromObject = result
                    textView.text = result?.detailedName
                } else if (requestKey == KEY_PLACE_TO) {
                    placeToObject = result
                    textView.text = result?.detailedName
                }
            }
        }
    }

    private fun setPassengerListener(textView: TextView) {
        parentFragmentManager.setFragmentResultListener(
            KEY_ADD_PASSENGER,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == KEY_ADD_PASSENGER) {
                val result = bundle.get(KEY_DATA) as List<*>
                adult = result[0].toString()
                child = result[1].toString()
                infant = result[2].toString()
                textView.text = getPassengerString()
                //TODO can change to get 3 strings in a bundle
            }
        }
    }

    private fun swapTextViewContent(textView1: TextView, textView2: TextView) {
        if (textView1.text.isNotEmpty() && textView2.text.isNotEmpty()) {
            textView1.text = textView2.text.also { textView2.text = textView1.text }
            placeFromObject = placeToObject.also { placeToObject = placeFromObject }

            val tempCode = localBasicFlight?.originCode
            val tempName = localBasicFlight?.originName
            localBasicFlight = localBasicFlight
                ?.destinationCode
                ?.let {
                    localBasicFlight?.copy(
                        originCode = it,
                        destinationCode = tempCode,
                        originName = localBasicFlight?.destinationName,
                        destinationName = tempName,
                    )
                }
        }
    }

    private fun findFlight() {
        if (placeFromIsValid()) {
            with(viewBinding) {
                val basicFlight = createBasicFlight()
                val dateFrom = extractString(textSearchDateFrom)
                val placeDestination = extractString(textSearchPlaceTo)
                val fragment: Fragment = if (dateFrom == null || placeDestination == null) {
                    BasicFlightsListFragment()
                } else {
                    Log.d(TAG, "findFlight: Called")
                    OfferFlightsListFragment()
                }
//                Log.d("TAG", "findFlight: $basicFlight")
                openFlightFragment(basicFlight, fragment)
            }
        }
    }

    private fun placeFromIsValid(): Boolean {
        with(viewBinding) {
            if (textSearchPlaceFrom.text.isEmpty()) {
                textPlaceFrom.setTextColor(resources.getColor(R.color.color_sunset_orange, null))
                scrollViewHome.fullScroll(View.FOCUS_UP)
                textSearchPlaceFrom.apply {
                    this.setHintTextColor(
                        resources.getColor(
                            R.color.color_sunset_orange,
                            null
                        )
                    )
                    this.hint = context.getString(R.string.text_field_required)
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
                }
                return false
            } else if (textSearchPlaceFrom.text.isNotEmpty() &&
                textSearchPlaceTo.text.isNotEmpty() &&
                textSearchDateTo.text.isNotEmpty()
            ) {
                //TODO change in main project
                textSearchDateTo.text = ""
            }
            return true
        }
    }

    private fun extractString(textView: TextView) =
        if (textView.text.isNotEmpty()) textView.text.toString() else null

    private fun createBasicFlight(): BasicFlight? {
        with(viewBinding) {
            var basicFlight: BasicFlight? = null
            val oneWay = switchOneway.isChecked
            val dateFrom = extractString(textSearchDateFrom)
            val dateTo = extractString(textSearchDateTo)
            val returnDate = extractString(textSearchReturnDate)
            val travelClass = spinnerTravelClass.selectedItem.toString()
            val currencyCode = spinnerCurrency.selectedItem.toString().split(" ").first()
            var departureDate: String? = null
            if (dateFrom == null || placeToObject == null) {
                dateFrom?.let { departureDate = it }
            } else {
                departureDate = dateFrom
            }
            dateTo?.let { departureDate += ",$it" }
            if (localBasicFlight == null) {
                placeFromObject?.let {
                    basicFlight = BasicFlight(
                        originCode = it.iataCode,
                        destinationCode = placeToObject?.iataCode,
                        originName = it.detailedName,
                        destinationName = placeToObject?.detailedName,
                        departureDate = departureDate,
                        oneWay = oneWay,
                        returnDate = returnDate,
                        adult = adult,
                        child = child,
                        infant = infant,
                        travelClass = travelClass,
                        currencyCode = currencyCode
                    )
                }
            } else {
                basicFlight = localBasicFlight?.copy()
                placeFromObject?.let {
                    basicFlight = localBasicFlight?.copy(
                        originCode = it.iataCode,
                        originName = it.detailedName
                    )
                }
                placeToObject?.let {
                    basicFlight = basicFlight?.copy(
                        destinationCode = it.iataCode,
                        destinationName = it.detailedName,
                    )
                }
                basicFlight = basicFlight?.copy(
                    departureDate = departureDate,
                    oneWay = oneWay,
                    returnDate = returnDate,
                    adult = adult,
                    child = child,
                    infant = infant,
                    travelClass = travelClass,
                    currencyCode = currencyCode
                )
            }
            return basicFlight
        }
    }

    private fun openFlightFragment(basicFlight: BasicFlight?, fragment: Fragment) {
        fragment.arguments = bundleOf(DATA_BASIC_FLIGHT to basicFlight)
        parentFragmentManager.addFragment(R.id.frameMain, fragment)
    }

    private fun getPassengerString(): String {
        var string = "$adult ${resources.getString(R.string.text_adult)}"
        if (child != NO_PASSENGER) string += ", $child ${resources.getString(R.string.text_child)}"
        if (infant != NO_PASSENGER) string += ", $infant ${resources.getString(R.string.text_infant)}"
        return string
    }

    override fun onPause() {
        super.onPause()
        createBasicFlight()?.let { presenter?.updateBasicFlight(it) }
    }

    companion object {
        const val KEY_PLACE_FROM = "from_place"
        const val KEY_PLACE_TO = "to_place"
        const val KEY_ADD_PASSENGER = "add_passenger"
        const val KEY_ADULT = "passenger_adult"
        const val KEY_CHILD = "passenger_child"
        const val KEY_INFANT = "passenger_infant"
        const val KEY_DATA = "data"
        const val NO_PASSENGER = "0"
        const val DEFAULT_ADULT_SEAT = "1"
        const val DATA_BASIC_FLIGHT = "basic_flight_data"

        private const val TAG = "HomeFragment"
    }
}
