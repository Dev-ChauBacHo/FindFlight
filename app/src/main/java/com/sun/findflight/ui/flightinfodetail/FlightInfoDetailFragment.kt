package com.sun.findflight.ui.flightinfodetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.model.Segment
import com.sun.findflight.databinding.FragmentFlightInfoDetailBinding
import com.sun.findflight.ui.flightinfodetail.adapter.SegmentAdapter
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import com.sun.findflight.utils.hide

class FlightInfoDetailFragment : BaseFragment<FragmentFlightInfoDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFlightInfoDetailBinding =
        FragmentFlightInfoDetailBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private val segmentAdapter by lazy {
        SegmentAdapter(
            mutableListOf(),
            this::itemSegmentClick
        )
    }

    override fun initComponents() {
        viewBinding.recyclerFlightsInfo.adapter = segmentAdapter
        viewBinding.progressBarFlightInfo.hide()
    }

    override fun initEvents() {
        viewBinding.recyclerFlightsInfo.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    containerContext.setBottomNavigationVisibility(false)
                } else {
                    containerContext.setBottomNavigationVisibility(true)
                }
            }
        })
    }

    override fun initData() {
        val bundle = this.arguments
        val flightDetail =
            bundle?.getParcelable<FlightDetail>(OfferFlightsListFragment.DATA_FLIGHT_DETAIL)
        flightDetail?.let {
            segmentAdapter.updateData(it.segments.toMutableList())
        }
    }

    override fun onDetach() {
        containerContext.setBottomNavigationVisibility(true)
        super.onDetach()
    }

    private fun itemSegmentClick(segment: Segment) = Unit
}
