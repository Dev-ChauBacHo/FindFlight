package com.sun.findflight.ui.favourite

import com.sun.findflight.R
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.repository.FlightRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightsFavouritePresenter(
    private val view: FlightsFavouriteContract.View,
    private val repository: FlightRepository
) : FlightsFavouriteContract.Presenter {

    override fun getLocalFlights() {
        try {
            view.showLoading()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        repository.getLocalFlights(object : OnDataCallBack<List<Flight>> {
            override fun onSuccess(data: List<Flight>) = dataAvailable(data)

            override fun onFailure(e: Exception?) = dataUnavailable()
        })
    }

    override fun deleteFlightFavourite(flight: Flight) {
        try {
            repository.deleteFlight(flight.id, object : OnDataCallBack<Boolean> {
                override fun onSuccess(data: Boolean) {
                    if (data) {
                        view.showMessage(R.string.text_deleted_from_favourite)
                        view.flightDeleted(flight)
                    } else {
                        view.showMessage(R.string.error_common)
                    }
                }

                override fun onFailure(e: Exception?) {
                    view.showMessage(R.string.error_common)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dataAvailable(data: List<Flight>) {
        try {
            view.hideLoading()
            view.showFlights(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dataUnavailable() {
        try {
            view.showMessage(R.string.error_cant_find_place)
            view.hideLoading()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getData() {
        getLocalFlights()
    }

    companion object {
        private const val TAG = "BasicFlightsListPresent"
        const val DATA_NOT_VALID = "-1"
    }
}
