package com.sun.findflight.ui.filterflights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.findflight.base.BaseSheetDialogFragment
import com.sun.findflight.databinding.FragmentFilterFlightsBinding
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.DEFAULT_MAX_PRICE
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_CURRENT_FILTER_PRICE
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_CURRENT_FILTER_STOPS
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_DATA_FILTER_PRICE
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_DATA_FILTER_STOPS
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_FILTER_FLIGHT

class FilterFlightFragment : BaseSheetDialogFragment<FragmentFilterFlightsBinding>(),
    View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilterFlightsBinding =
        FragmentFilterFlightsBinding::inflate

    override fun initEvents() = with(viewBinding) {
        listOf(
            buttonFilterApply,
            buttonFilterCancel,
            buttonFilterClear
        ).forEach { it.setOnClickListener(this@FilterFlightFragment) }
    }

    override fun initData() {
        val maxPrice = arguments?.getInt(KEY_CURRENT_FILTER_PRICE)
        val numStops = arguments?.getInt(KEY_CURRENT_FILTER_STOPS)
        if (maxPrice != null && maxPrice > DEFAULT_MAX_PRICE) viewBinding.editTextMaxPrice.setText(maxPrice.toString())
        if (numStops != null && numStops > 0) viewBinding.editTextNumStop.setText(numStops.toString())
    }

    override fun onClick(v: View?) = with(viewBinding) {
        when (v) {
            buttonFilterClear -> {
                editTextMaxPrice.text.clear()
                editTextNumStop.text.clear()
            }
            buttonFilterCancel -> this@FilterFlightFragment.dismiss()
            buttonFilterApply -> setResult(
                editTextMaxPrice.text.toString(),
                editTextNumStop.text.toString()
            )
        }
    }

    private fun setResult(price: String, stops: String) {
        val bundle = Bundle().apply {
            price.toIntOrNull()?.let { putInt(KEY_DATA_FILTER_PRICE, it) }
            stops.toIntOrNull()?.let { putInt(KEY_DATA_FILTER_STOPS, it) }
        }
        parentFragmentManager.setFragmentResult(
            KEY_FILTER_FLIGHT,
            bundle
        )
        this@FilterFlightFragment.dismiss()
    }
}
