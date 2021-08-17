package com.sun.findflight.ui.basicflightslist

import com.sun.findflight.R
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.repository.FlightRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack
import com.sun.findflight.utils.BaseConst
import org.json.JSONException
import org.json.JSONObject

class BasicFlightsListPresenter(
    private val view: BasicFlightsListContract.View,
    private val repository: FlightRepository
) : BasicFlightsListContract.Presenter {

    override fun getFlights(basicFlight: BasicFlight) {
        repository.getFlights(basicFlight, object : OnDataCallBack<List<Flight>> {
            override fun onSuccess(data: List<Flight>) {
                if (data.isEmpty()) {
                    dataUnavailable()
                } else {
                    getLocalBasicFlight(data)
                }
            }

            override fun onFailure(e: Exception?) = dataUnavailable()
        })
    }

    private fun getLocalBasicFlight(dataFlight: List<Flight>) {
        repository.isFavorite(dataFlight, object : OnDataCallBack<Unit> {
            override fun onSuccess(data: Unit) = dataAvailable(dataFlight)

            override fun onFailure(e: Exception?) = dataAvailable(dataFlight)
        })
    }

    override fun insertFlightFavourite(flight: Flight) {
        try {
            repository.insertFlight(flight, object : OnDataCallBack<String> {
                override fun onSuccess(data: String) {
                    if (data == DATA_NOT_VALID) {
                        view.showMessage(R.string.error_common)
                    } else {
                        flight.id = data
                        view.showMessage(R.string.text_added_to_favourite)
                        println(flight)
                    }
                }

                override fun onFailure(e: Exception?) = view.showMessage(R.string.error_common)

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteFlightFavourite(flight: Flight) {
        try {
            repository.deleteFlight(flight.id, object : OnDataCallBack<Boolean> {
                override fun onSuccess(data: Boolean) {
                    if (data) {
                        view.showMessage(R.string.text_deleted_from_favourite)
                    } else {
                        view.showMessage(R.string.error_common)
                    }
                }

                override fun onFailure(e: Exception?) = view.showMessage(R.string.error_common)
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

    }

    companion object {
        private const val TAG = "BasicFlightsListPresent"
        const val DATA_NOT_VALID = "-1"
    }
}
