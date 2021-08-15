package com.sun.findflight.ui.offerflightslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.databinding.FragmentOfferFlightsListBinding
import com.sun.findflight.ui.filterflights.FilterFlightFragment
import com.sun.findflight.ui.flightdetail.FlightDetailFragment
import com.sun.findflight.ui.home.HomeFragment
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.adapter.OfferFlightsListAdapter
import com.sun.findflight.ui.sortflights.SortFlightFragment
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_DEFAULT
import com.sun.findflight.utils.RepositoryUtils
import com.sun.findflight.utils.addFragment
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.showToast

class OfferFlightsListFragment :
    BaseFragment<FragmentOfferFlightsListBinding>(),
    OfferFlightsListContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOfferFlightsListBinding =
        FragmentOfferFlightsListBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private val flightDetailListAdapter by lazy {
        OfferFlightsListAdapter(
            mutableListOf(),
            this::itemFlightDetailClick
        )
    }
    private var presenter: OfferFlightsListPresenter? = null
    private var sortCode = KEY_SORT_DEFAULT
    private var maxPrice = DEFAULT_MAX_PRICE
    private var numStops = DEFAULT_NUM_STOP

    override fun initComponents() {
        viewBinding.recyclerFlightsOffer.adapter = flightDetailListAdapter
        containerContext.setBackButtonStatus(true)
    }

    override fun initEvents() {
        viewBinding.buttonSort.setOnClickListener {
            val fragment = SortFlightFragment()
            fragment.arguments = bundleOf(KEY_CURRENT_SORT_CODE to sortCode)
            fragment.show(parentFragmentManager, null)
        }
        viewBinding.buttonFilter.setOnClickListener {
            val fragment = FilterFlightFragment()
            fragment.arguments = Bundle().apply {
                if (maxPrice > DEFAULT_MAX_PRICE) putInt(KEY_DATA_FILTER_PRICE, maxPrice)
                if (numStops > DEFAULT_NUM_STOP) putInt(KEY_DATA_FILTER_STOPS, numStops)
            }
            fragment.show(parentFragmentManager, null)
        }
        viewBinding.recyclerFlightsOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    containerContext.setBottomNavigationVisibility(false)
                } else {
                    containerContext.setBottomNavigationVisibility(true)
                }
            }
        })
        setSortListener()
        setFilterListener()
    }

    override fun initData() {
        val repository = RepositoryUtils.getFlightOfferRepository()
        presenter = OfferFlightsListPresenter(this, repository)

        val bundle = this.arguments
        val basicFlight = bundle?.getParcelable<BasicFlight>(HomeFragment.DATA_BASIC_FLIGHT)
        basicFlight?.let { presenter?.getOfferFlights(it) }
    }

    override fun showOfferFlights(flights: List<FlightDetail>) {
        flightDetailListAdapter.updateData(flights.toMutableList())

    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {
//        flightDetailListAdapter.updateData(mutableListOf())
//        viewBinding.progressBarFlightOffer.show()
    }

    override fun hideLoading() {
        viewBinding.progressBarFlightOffer.hide()
    }

    private fun itemFlightDetailClick(flightDetail: FlightDetail) {
        val fragment = FlightDetailFragment()
        fragment.arguments = bundleOf(DATA_FLIGHT_DETAIL to flightDetail)
        parentFragmentManager.addFragment(R.id.frameMain, fragment)
    }

    private fun setSortListener() =
        parentFragmentManager.setFragmentResultListener(
            KEY_SORT_FLIGHT,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == KEY_SORT_FLIGHT) {
                sortCode = bundle.getInt(KEY_DATA_SORT)
                flightDetailListAdapter.sortBy(sortCode)
            }
        }

    private fun setFilterListener() =
        parentFragmentManager.setFragmentResultListener(
            KEY_FILTER_FLIGHT,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == KEY_FILTER_FLIGHT) {
                maxPrice = bundle.getInt(KEY_DATA_FILTER_PRICE)
                numStops = bundle.getInt(KEY_DATA_FILTER_STOPS)
//                Log.d(TAG, "setFilterListener: $maxPrice $numStops")
                if (maxPrice == 0 && numStops == 0) {
                    sortCode = KEY_SORT_DEFAULT
                    flightDetailListAdapter.restoreToDefault()
                } else if (maxPrice != 0 && numStops != 0) {
                    flightDetailListAdapter.filterByPriceAndNumStops(maxPrice, numStops)
                } else {
                    if (maxPrice > DEFAULT_MAX_PRICE) flightDetailListAdapter.filterByPrice(maxPrice)
                    if (numStops > 0) flightDetailListAdapter.filterByNumStops(
                        numStops
                    )
                }
            }
        }

    override fun onDetach() {
        containerContext.setBottomNavigationVisibility(true)
        super.onDetach()
    }

    companion object {
        private const val TAG = "OfferFlightsListFragmen"
        const val DEFAULT_MAX_PRICE = 0
        const val DEFAULT_NUM_STOP = 0
        const val DATA_FLIGHT_DETAIL = "detail_flight_data"
        const val KEY_SORT_FLIGHT = "sort_flight"
        const val KEY_FILTER_FLIGHT = "filter_flight"
        const val KEY_DATA_SORT = "sort_data"
        const val KEY_DATA_FILTER_PRICE = "filter_price"
        const val KEY_DATA_FILTER_STOPS = "filter_stops"
        const val KEY_CURRENT_SORT_CODE = "current_sort"
        const val KEY_CURRENT_FILTER_PRICE = "filter_price"
        const val KEY_CURRENT_FILTER_STOPS = "filter_stops"
    }
}
