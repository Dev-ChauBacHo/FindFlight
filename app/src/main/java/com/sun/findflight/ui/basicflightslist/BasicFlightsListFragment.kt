package com.sun.findflight.ui.basicflightslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight
import com.sun.findflight.databinding.FragmentBasicFlightsListBinding
import com.sun.findflight.ui.basicflightslist.adapter.BasicFlightListAdapter
import com.sun.findflight.ui.home.HomeFragment
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.offerflightslist.OfferFlightsListFragment
import com.sun.findflight.utils.*

class BasicFlightsListFragment :
    BaseFragment<FragmentBasicFlightsListBinding>(),
    BasicFlightsListContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBasicFlightsListBinding =
        FragmentBasicFlightsListBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private var presenter: BasicFlightsListPresenter? = null
    private var basicFlight: BasicFlight? = null
    private val flightListAdapter by lazy {
        BasicFlightListAdapter(
            mutableListOf(),
            this::itemFlightClick,
            this::favouriteClick
        )
    }

    override fun initComponents() {
        viewBinding.recyclerFlights.adapter = flightListAdapter
        containerContext.setBackButtonStatus(true)
    }

    override fun initEvents() {
        viewBinding.recyclerFlights.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
        val repository = context?.let { RepositoryUtils.getFlightRepository(it) }
        presenter = repository?.let { BasicFlightsListPresenter(this, it) }

        val bundle = this.arguments
        basicFlight = bundle?.getParcelable(HomeFragment.DATA_BASIC_FLIGHT)
        basicFlight?.let { getFlights(it) }
    }

    override fun showFlights(flights: List<Flight>) {
        flightListAdapter.updateData(flights.toMutableList())
    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {
//        flightListAdapter.updateData(mutableListOf())
//        viewBinding.progressBarFlight.show()
    }

    override fun hideLoading() {
        viewBinding.progressBarFlight.hide()
    }

    private fun favouriteClick(status: Boolean, flight: Flight) {
        if (status) {
            presenter?.insertFlightFavourite(flight)
        } else {
            presenter?.deleteFlightFavourite(flight)
        }
    }

    private fun getFlights(basicFlight: BasicFlight) {
        presenter?.getFlights(basicFlight)
    }

    private fun itemFlightClick(flight: Flight) {
        val bundleFlight = basicFlight?.copy(
            originName = flight.originName,
            destinationName = flight.destinationName,
            destinationCode = flight.destinationCode,
            departureDate = flight.departureDate,
            returnDate = flight.returnDate,
        )
        val fragment = OfferFlightsListFragment()
        fragment.arguments = bundleOf(HomeFragment.DATA_BASIC_FLIGHT to bundleFlight)
        parentFragmentManager.addFragment(R.id.frameMain, fragment)
    }

    override fun onDetach() {
        with(containerContext) {
            setBackButtonStatus(false)
            setBottomNavigationVisibility(true)
        }
        super.onDetach()
    }

    companion object {
        private const val TAG = "BasicFlightsListFragmen"
    }
}
