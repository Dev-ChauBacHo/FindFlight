package com.sun.findflight.ui.basicflightslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.Flight
import com.sun.findflight.databinding.ItemFlightBinding
import com.sun.findflight.utils.ColorUtil
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.show

class BasicFlightListAdapter(
    items: MutableList<Flight>,
    private val onItemClick: (Flight) -> Unit,
    private val onFavouriteClick: (Boolean, Flight) -> Unit
) : BaseAdapter<Flight, BasicFlightListAdapter.FlightListViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FlightListViewHolder(
            ItemFlightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onFavouriteClick, onItemClick
        )

    class FlightListViewHolder(
        private val viewBinding: ItemFlightBinding,
        private val onFavouriteClick: (Boolean, Flight) -> Unit,
        onItemClick: (Flight) -> Unit
    ) : BaseViewHolder<Flight>(viewBinding, onItemClick) {

        override fun bindData(itemData: Flight) {
            super.bindData(itemData)
            val color = ColorUtil.getRandomColor()
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
                    if (isFavourite == FAVOURITE) {
                        imageFavourite.show()
                        imageUnFavourite.hide()
                    } else {
                        imageUnFavourite.show()
                        imageFavourite.hide()
                    }
                }
                imageFavourite.setOnClickListener {
                    onFavouriteClick(false, itemData)
                    it.hide()
                    imageUnFavourite.show()
                }
                imageUnFavourite.setOnClickListener {
                    onFavouriteClick(true, itemData)
                    it.hide()
                    imageFavourite.show()
                }
            }
        }
    }

    companion object {
        const val FAVOURITE = 1
        private const val TAG = "BasicFlightListAdapter"
    }
}
