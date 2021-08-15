package com.sun.findflight.ui.searchPlace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.Place
import com.sun.findflight.data.source.remote.api.ApiQuery
import com.sun.findflight.databinding.ItemPlaceBinding
import com.sun.findflight.utils.loadImage
import java.util.*

class PlaceListAdapter(private val onItemClick: (Place) -> Unit, items: MutableList<Place>) :
    BaseAdapter<Place, PlaceListAdapter.PlaceListViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListViewHolder =
        PlaceListViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), onItemClick
        )
// TODO set background to selectable in main project
// TODO Change background of item_place to a shadow one
    class PlaceListViewHolder(
        private val viewBinding: ItemPlaceBinding,
        onItemClick: (Place) -> Unit
    ) : BaseViewHolder<Place>(viewBinding, onItemClick) {

        override fun bindData(itemData: Place) {
            super.bindData(itemData)
            //TODO remove image in itemData and get image url by countryCode in address
            itemData.image =
                ApiQuery.queryImage(itemData.address.countryCode.lowercase(Locale.getDefault()))
            with(viewBinding) {
                itemData.run {
                    textShowPlaceType.text = subType
                    textShowPlaceName.text = name
                    textShowPlaceDetail.text = detailedName
                    textShowPlaceCity.text = address.cityName
                    textShowPlaceCountry.text = address.countryName
                    textShowPlaceTimezone.text = timeZone
                    try {
                        image?.let { imagePlace.loadImage(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
