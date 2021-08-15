package com.sun.findflight.ui.offerflightslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.R
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.remote.api.ApiQuery
import com.sun.findflight.databinding.ItemFlightDetailBinding
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_DEFAULT
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_HIGHEST_PRICE
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_LARGEST_SEATS
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_LONGEST_DURATION
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_LOWEST_PRICE
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_LOWEST_SEATS
import com.sun.findflight.ui.sortflights.SortFlightFragment.Companion.KEY_SORT_SHORTEST_DURATION
import com.sun.findflight.utils.TimeUtils
import com.sun.findflight.utils.loadImage
import java.util.*

class OfferFlightsListAdapter(
    private val items: MutableList<FlightDetail>,
    private val onItemClick: (FlightDetail) -> Unit
) : BaseAdapter<FlightDetail, OfferFlightsListAdapter.FlightListViewHolder>(items) {

    private val storeItems = mutableListOf<FlightDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightListViewHolder =
        FlightListViewHolder(
            ItemFlightDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    override fun updateData(newData: MutableList<FlightDetail>) {
        super.updateData(newData)
        storeItems.clear()
        storeItems.addAll(items)
    }

    fun sortBy(code: Int) {
        when (code) {
            KEY_SORT_DEFAULT -> {
                items.clear()
                items.addAll(storeItems)
            }
            KEY_SORT_LOWEST_PRICE -> items.sortBy { it.currency.price.toDoubleOrNull() }
            KEY_SORT_HIGHEST_PRICE -> items.sortByDescending { it.currency.price.toDoubleOrNull() }
            KEY_SORT_SHORTEST_DURATION -> items.sortWith { flight1, flight2 ->
                val duration1 = TimeUtils.convertTimeToMinute(flight1.duration)
                val duration2 = TimeUtils.convertTimeToMinute(flight2.duration)
                duration1.compareTo(duration2)
            }
            KEY_SORT_LONGEST_DURATION -> items.sortWith { flight1, flight2 ->
                val duration1 = TimeUtils.convertTimeToMinute(flight1.duration)
                val duration2 = TimeUtils.convertTimeToMinute(flight2.duration)
                duration2.compareTo(duration1)
            }
            KEY_SORT_LOWEST_SEATS -> items.sortBy { it.numberOfBookableSeats.toIntOrNull() }
            KEY_SORT_LARGEST_SEATS -> items.sortByDescending { it.numberOfBookableSeats.toIntOrNull() }
        }

        notifyDataSetChanged()
    }

    fun restoreToDefault() = super.updateData(storeItems)

    fun filterByPrice(maxPrice: Int) {
//        Log.d("TAG", "filterByPrice: Called")
        restoreToDefault()
        val tempList = filterByPriceOnly(maxPrice)
        super.updateData(tempList.toMutableList())
    }

    fun filterByNumStops(numStop: Int) {
        restoreToDefault()
        val tempList = filterByNumStopsOnly(numStop, items)
        super.updateData(tempList.toMutableList())
    }

    fun filterByPriceAndNumStops(maxPrice: Int, numStop: Int) {
        restoreToDefault()
        var tempList = filterByPriceOnly(maxPrice)
        tempList = filterByNumStopsOnly(numStop, tempList.toMutableList())
        super.updateData(tempList.toMutableList())
    }

    private fun filterByPriceOnly(maxPrice: Int) =
        items.filter {
            val currentPrice = it.currency.price.toDoubleOrNull()
            if (currentPrice != null) {
                currentPrice <= maxPrice
            } else {
                false
            }
        }

    private fun filterByNumStopsOnly(numStop: Int, list: MutableList<FlightDetail>) =
        list.filter { it.segments.size == numStop }

    class FlightListViewHolder(
        private val viewBinding: ItemFlightDetailBinding,
        onItemClick: (FlightDetail) -> Unit,
    ) : BaseViewHolder<FlightDetail>(viewBinding, onItemClick) {

        override fun bindData(itemData: FlightDetail) {
            super.bindData(itemData)
            var carrierList = ""
            with(viewBinding) {
                itemData.run {
                    textFlightOrigin.text = basicFlight?.originCode
                    textFlightDes.text = basicFlight?.destinationCode
                    textFlightDuration.text = duration.removeRange(0, 2)
                    if (segments.size == ONE_SEGMENT) {
                        textFlightStopNum.text = itemView.context.getString(R.string.text_direct)
                    } else {
                        val stop =
                            "${segments.size} ${itemView.context.getString(R.string.text_stops)}"
                        textFlightStopNum.text = stop
                    }
                    textShowDepartureDate.text = basicFlight?.departureDate
                    val price =
                        "${itemView.context.getString(R.string.text_price)} (${currency.currencyCode})"
                    textPrice.text = price
                    textShowPrice.text = currency.price
                    airlinesCode.forEach { carrierList += "$it," }
                    carrierList = carrierList.removeSuffix(COMMA)
                    textShowCarrier.text = carrierList
                    textShowSeatNum.text = numberOfBookableSeats
                    try {
                        segments.first().departure.countryCode?.let {
                            imageFlightOrigin.loadImage(
                                ApiQuery.queryImage(it.lowercase(Locale.getDefault()))
                            )
                        }
                        segments.last().arrival.countryCode?.let {
                            imageFlightDes.loadImage(
                                ApiQuery.queryImage(it.lowercase(Locale.getDefault()))
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        companion object {
            const val ONE_SEGMENT = 1
            const val COMMA = ","
        }
    }
}
