package com.sun.findflight.ui.flightfaredetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.R
import com.sun.findflight.base.BaseAdapter
import com.sun.findflight.base.BaseViewHolder
import com.sun.findflight.data.model.SegmentFareDetail
import com.sun.findflight.databinding.ItemFlightFareBinding

class FareAdapter(
    items: MutableList<SegmentFareDetail>,
    private val onItemClick: (SegmentFareDetail) -> Unit
) : BaseAdapter<SegmentFareDetail, FareAdapter.FareViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FareViewHolder(
            ItemFlightFareBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    class FareViewHolder(
        private val viewBinding: ItemFlightFareBinding,
        onItemClick: (SegmentFareDetail) -> Unit,
    ) : BaseViewHolder<SegmentFareDetail>(viewBinding, onItemClick) {

        override fun bindData(itemData: SegmentFareDetail) {
            super.bindData(itemData)
            with(viewBinding) {
                itemData.run {
                    textCabin.text = cabin
                    val fare = "${itemView.context.getString(R.string.text_class)} $fareClass"
                    textClass.text = fare
                    if (weightCheckedBad == null) {
                        textBaggage.text = itemView.context.getString(R.string.text_unknown)
                    } else {
                        val unit =
                            "$weightCheckedBad ${itemView.context.getString(R.string.text_weight_unit)}"
                        textBaggage.text = unit
                    }
                }
            }
        }
    }
}
