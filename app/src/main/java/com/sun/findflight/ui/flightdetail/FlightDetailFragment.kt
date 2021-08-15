package com.sun.findflight.ui.flightdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.databinding.FragmentFlightDetailBinding
import com.sun.findflight.ui.flightdetail.adapter.FlightDetailStateAdapter
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import com.sun.findflight.utils.RepositoryUtils
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.showToast

class FlightDetailFragment :
    BaseFragment<FragmentFlightDetailBinding>(),
    FlightDetailContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFlightDetailBinding =
        FragmentFlightDetailBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private var presenter: FlightDetailPresenter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        containerContext.setBottomNavigationVisibility(false)
    }

    override fun initComponents() {

    }

    override fun initEvents() {

    }

    override fun initData() {
//        flightDetail = arguments?.getParcelable(OfferFlightsListFragment.DATA_FLIGHT_DETAIL)
        val repository = RepositoryUtils.getFlightInfoRepository()
        presenter = FlightDetailPresenter(this, repository)

        val flightDetail =
            arguments?.getParcelable<FlightDetail>(OfferFlightsListFragment.DATA_FLIGHT_DETAIL)
        flightDetail?.let { presenter?.updateFlight(flightDetail) }
    }

    override fun showFlight(flightDetail: FlightDetail) {
        val stateAdapter = activity?.let { FlightDetailStateAdapter(it, flightDetail) }
        stateAdapter?.let { viewBinding.viewPagerFlights.adapter = it }
        try {
            TabLayoutMediator(
                viewBinding.tabLayoutFlight,
                viewBinding.viewPagerFlights
            ) { tab, position ->
                if (position == INFO_TAB_POSITION) {
                    tab.text = getString(R.string.text_info)
                } else {
                    tab.text = getString(R.string.text_fare)
                }
            }.attach()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {

    }

    override fun hideLoading() = viewBinding.progressBarFare.hide()

    companion object {
        const val INFO_TAB_POSITION = 0
    }
}
