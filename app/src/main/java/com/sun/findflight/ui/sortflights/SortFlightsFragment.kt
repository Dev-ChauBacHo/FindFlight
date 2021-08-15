package com.sun.findflight.ui.sortflights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.sun.findflight.base.BaseSheetDialogFragment
import com.sun.findflight.databinding.FragmentSortFlightsBinding
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_DATA_SORT
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment.Companion.KEY_SORT_FLIGHT
import com.sun.findflight.utils.setChecked

class SortFlightFragment : BaseSheetDialogFragment<FragmentSortFlightsBinding>(),
    View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSortFlightsBinding =
        FragmentSortFlightsBinding::inflate

    override fun initEvents() = with(viewBinding) {
        listOf(
            radioNoSort,
            radioLowestPrice,
            radioHighestPrice,
            radioShortestDuration,
            radioLongestDuration,
            radioSmallestAvailableSeats,
            radioLargestAvailableSeats
        ).forEach { it.setOnClickListener(this@SortFlightFragment) }
    }

    override fun initData() {
        val currentSortCode = arguments?.getInt(OfferFlightsListFragment.KEY_CURRENT_SORT_CODE)
        with(viewBinding) {
            when (currentSortCode) {
                KEY_SORT_DEFAULT -> radioNoSort.setChecked()
                KEY_SORT_LOWEST_PRICE -> radioLowestPrice.setChecked()
                KEY_SORT_HIGHEST_PRICE -> radioHighestPrice.setChecked()
                KEY_SORT_SHORTEST_DURATION -> radioShortestDuration.setChecked()
                KEY_SORT_LONGEST_DURATION -> radioLongestDuration.setChecked()
                KEY_SORT_LOWEST_SEATS -> radioSmallestAvailableSeats.setChecked()
                KEY_SORT_LARGEST_SEATS -> radioLargestAvailableSeats.setChecked()
            }
        }
    }

    override fun onClick(v: View?) = with(viewBinding) {
        when (v) {
            radioNoSort -> setResult(KEY_SORT_DEFAULT)
            radioLowestPrice -> setResult(KEY_SORT_LOWEST_PRICE)
            radioHighestPrice -> setResult(KEY_SORT_HIGHEST_PRICE)
            radioShortestDuration -> setResult(KEY_SORT_SHORTEST_DURATION)
            radioLongestDuration -> setResult(KEY_SORT_LONGEST_DURATION)
            radioSmallestAvailableSeats -> setResult(KEY_SORT_LOWEST_SEATS)
            radioLargestAvailableSeats -> setResult(KEY_SORT_LARGEST_SEATS)
        }
    }

    private fun setResult(value: Int) {
        parentFragmentManager.setFragmentResult(
            KEY_SORT_FLIGHT,
            bundleOf(KEY_DATA_SORT to value)
        )
        this@SortFlightFragment.dismiss()
    }

    companion object {
        const val KEY_SORT_DEFAULT = 200
        const val KEY_SORT_LOWEST_PRICE = 201
        const val KEY_SORT_HIGHEST_PRICE = 202
        const val KEY_SORT_SHORTEST_DURATION = 203
        const val KEY_SORT_LONGEST_DURATION = 204
        const val KEY_SORT_LOWEST_SEATS = 205
        const val KEY_SORT_LARGEST_SEATS = 206
    }
}
