package com.sun.findflight.ui.flightinfodetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.sun.findflight.R
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.Segment
import com.sun.findflight.data.source.remote.api.ApiQuery
import com.sun.findflight.databinding.ItemFlightInfoBinding
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.loadImage
import com.sun.findflight.utils.show
import java.util.*

class SegmentAdapter(
    items: MutableList<Segment>,
    private val onItemClick: (Segment) -> Unit
) : BaseAdapter<Segment, SegmentAdapter.SegmentViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SegmentViewHolder(
            ItemFlightInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    class SegmentViewHolder(
        private val viewBinding: ItemFlightInfoBinding,
        onItemClick: (Segment) -> Unit,
    ) : BaseViewHolder<Segment>(viewBinding, onItemClick) {

        override fun bindData(itemData: Segment) {
            super.bindData(itemData)
            with(viewBinding) {
                itemData.run {
                    textFlightInfoOrigin.text = departure.name
                    textDepartureTime.text = departure.time.replace(OLD_VALUE, NEW_VALUE)
                    textDepartureTimezone.text = departure.timezone
                    textFlightShowCarrier.text = carrierName
                    textFlightShowCarrierNumber.text = carrierNumber
                    textFlightShowAircraft.text = aircraftName
                    textShowDepartureTerminal.text = departure.terminal
                    textShowArrivalTerminal.text = arrival.terminal
                    textArrivalTime.text = arrival.time.replace(OLD_VALUE, NEW_VALUE)
                    textDestinationTimezone.text = arrival.timezone
                    textFlightInfoDes.text = arrival.name
                    departure.countryCode?.let { loadToImage(it, imageFlightInfoOrigin) }
                    arrival.countryCode?.let { loadToImage(it, imageFlightInfoDes) }

                    if (layover != null) {
                        val stringLayover =
                            "$layover ${itemView.context.getString(R.string.text_layover)} ${arrival.name}"
                        showLayoverView(stringLayover)
                    } else {
                        hideLayoverView()
                    }
                }
            }
        }

        private fun loadToImage(countryCode: String, imageView: ImageView) {
            try {
                imageView.loadImage(ApiQuery.queryImage(countryCode.lowercase(Locale.getDefault())))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun showLayoverView(text: String) = with(viewBinding) {
            imageFlightInfoLayover.show()
            textLayover.show()
            textLayover.text = text
        }

        private fun hideLayoverView() = with(viewBinding) {
            imageFlightInfoLayover.hide()
            textLayover.hide()
        }

        companion object {
            const val OLD_VALUE = "T"
            const val NEW_VALUE = " "
        }
    }
}
