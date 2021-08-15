package com.sun.findflight.ui.flightfaredetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.model.SegmentFareDetail
import com.sun.findflight.data.model.Traveler
import com.sun.findflight.databinding.FragmentFlightFareDetailBinding
import com.sun.findflight.ui.flightfaredetail.adapter.FareAdapter
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import kotlin.math.roundToInt

class FlightFareDetailFragment : BaseFragment<FragmentFlightFareDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFlightFareDetailBinding =
        FragmentFlightFareDetailBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private val fareAdapter by lazy {
        FareAdapter(
            mutableListOf(),
            this::itemFareClick
        )
    }

    override fun initComponents() {
        viewBinding.recyclerFare.adapter = fareAdapter
        containerContext.setBottomNavigationVisibility(false)
    }

    override fun initEvents() {

    }

    override fun initData() {
        val bundle = this.arguments
        val flightDetail =
            bundle?.getParcelable<FlightDetail>(OfferFlightsListFragment.DATA_FLIGHT_DETAIL)
        flightDetail?.let { fareAdapter.updateData(it.travelers.first().segmentFareDetail.toMutableList()) }
        calculateAndShowPrice(flightDetail)
    }

    private fun calculateAndShowPrice(flightDetail: FlightDetail?) {
        var calculatePrice = INIT_DOUBLE
        flightDetail?.travelers?.forEach {
            showDetailSegment(it)
            it.price.toDoubleOrNull()?.let { price -> calculatePrice += price }
        }
        flightDetail?.basicFlight?.let { showDetailPrice(it, calculatePrice) }
    }

    private fun showDetailSegment(traveler: Traveler) = with(viewBinding) {
        traveler.run {
//            Log.d("TAG", "showDetailSegment: $fareOption")
            when {
                travelerType.equals(ADULT, true) -> {
                    textAdultOption.text = fareOption
                    textAdultPrice.text = price
                }
                travelerType.equals(CHILD, true) -> {
                    Log.d("TAG", "showDetailSegment: $fareOption")
                    textChildOption.text = fareOption
                    textChildPrice.text = price
                }
                travelerType.equals(INFANT, true) ->
                    textInfantPrice.text = price
            }
        }
    }

    private fun showDetailPrice(basicFlight: BasicFlight, calculatedPrice: Double) =
        with(viewBinding) {
            var quantity = "$QUANTITY_SYMBOL${basicFlight.adult}"
            textAdultQuantity.text = quantity
            quantity = "$QUANTITY_SYMBOL${basicFlight.child}"
            textChildQuantity.text = quantity
            quantity = "$QUANTITY_SYMBOL${basicFlight.infant}"
            textInfantQuantity.text = quantity

            val totalPrice = "${calculatedPrice.roundToInt()} ${basicFlight.currencyCode}"
            textTotalPrice.text = totalPrice
        }

    private fun itemFareClick(segmentFareDetail: SegmentFareDetail) = Unit

    override fun onDetach() {
        containerContext.setBottomNavigationVisibility(true)
        super.onDetach()
    }

    companion object {
        private const val ADULT = "ADULT"
        private const val CHILD = "CHILD"
        private const val INFANT = "HELD_INFANT"
        const val INIT_DOUBLE = 0.0
        const val QUANTITY_SYMBOL = "X"
    }
}
