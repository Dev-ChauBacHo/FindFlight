package com.sun.findflight.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.Flight
import com.sun.findflight.databinding.ItemFlightBinding
import com.sun.findflight.utils.ColorUtil
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.show

class FlightFavouriteAdapter(
    private val items: MutableList<Flight>,
    private val onItemClick: (Flight) -> Unit,
    private val onFavouriteClick: (Flight) -> Unit
) : BaseAdapter<Flight, FlightFavouriteAdapter.FlightListViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FlightListViewHolder(
            ItemFlightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onFavouriteClick, onItemClick
        )

    fun removeItem(flight: Flight) {
        val position = items.indexOf(flight)
        if (position != POSITION_INVALID) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    class FlightListViewHolder(
        private val viewBinding: ItemFlightBinding,
        private val onFavouriteClick: (Flight) -> Unit,
        onItemClick: (Flight) -> Unit
    ) : BaseViewHolder<Flight>(viewBinding, onItemClick) {

        override fun bindData(itemData: Flight) {
            super.bindData(itemData)
            val color = ColorUtil.getRandomColor()
            itemView.foreground = null
            with(viewBinding) {
                itemData.run {
                    textOriginPlaceCode.text = originCode
                    textOriginPlaceName.text = originName
                    textDestinationPlaceCode.text = destinationCode
                    textDestinationPlaceName.text = destinationName
                    textBasicDepartureDate.text = departureDate
                    textBasicReturnDate.text = returnDate
                    listOf(
                        imagePlane1,
                        imagePlane3
                    ).forEach {
                        it.setColorFilter(color)
                    }
                    imageFavourite.show()
                    imageUnFavourite.hide()
                }
                imageFavourite.setOnClickListener {
                    onFavouriteClick(itemData)
                    it.hide()
                    imageUnFavourite.show()
                }
            }
        }
    }

    companion object {
        const val POSITION_INVALID = -1
    }
}
