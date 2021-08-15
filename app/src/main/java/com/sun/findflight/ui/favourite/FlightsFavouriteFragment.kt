package com.sun.findflight.ui.favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.Flight
import com.sun.findflight.databinding.FragmentBasicFlightsListBinding
import com.sun.findflight.ui.favourite.adapter.FlightFavouriteAdapter
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.utils.RepositoryUtils
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.show
import com.sun.findflight.utils.showToast

class FlightsFavouriteFragment :
    BaseFragment<FragmentBasicFlightsListBinding>(),
    FlightsFavouriteContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBasicFlightsListBinding =
        FragmentBasicFlightsListBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private var presenter: FlightsFavouritePresenter? = null

    //    private var basicFlight: BasicFlight? = null
    private val flightListAdapter by lazy {
        FlightFavouriteAdapter(
            mutableListOf(),
            this::itemFlightClick,
            this::onFavouriteClick
        )
    }

    override fun initComponents() {
        viewBinding.recyclerFlights.adapter = flightListAdapter
        containerContext.changeTitle(getString(R.string.title_favorite))
        hideLoading()
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
        presenter = repository?.let { FlightsFavouritePresenter(this, it) }
        presenter?.getData()
    }

    override fun showFlights(flights: List<Flight>) {
        flightListAdapter.updateData(flights.toMutableList())
    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {
        flightListAdapter.updateData(mutableListOf())
        viewBinding.progressBarFlight.show()
    }

    override fun hideLoading() {
        viewBinding.progressBarFlight.hide()
    }

    override fun flightDeleted(flight: Flight) {
        flightListAdapter.removeItem(flight)
    }

    private fun onFavouriteClick(flight: Flight) {
        presenter?.deleteFlightFavourite(flight)
    }

    private fun itemFlightClick(flight: Flight) = Unit

    companion object {
        private const val TAG = "FlightsFavouriteFragmen"
    }
}
