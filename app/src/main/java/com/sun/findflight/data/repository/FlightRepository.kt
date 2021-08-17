package com.sun.findflight.data.repository

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.source.FlightDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightRepository private constructor(
    private val remote: FlightDataSource.Remote,
    private val local: FlightDataSource.Local
) : FlightDataSource.Remote, FlightDataSource.Local {

    override fun getFlights(
        basicFlight: BasicFlight,
        callBack: OnDataCallBack<List<Flight>>
    ) {
        remote.getFlights(basicFlight, callBack)
    }

    override fun getLocalFlights(callBack: OnDataCallBack<List<Flight>>) {
        local.getLocalFlights(callBack)
    }

    override fun insertFlight(flight: Flight, callBack: OnDataCallBack<String>) {
        local.insertFlight(flight, callBack)
    }

    override fun deleteFlight(flightId: String, callBack: OnDataCallBack<Boolean>) {
        local.deleteFlight(flightId, callBack)
    }

    override fun isFavorite(flights: List<Flight>, callBack: OnDataCallBack<Unit>) {
        local.isFavorite(flights, callBack)
    }

    companion object {
        private var instance: FlightRepository? = null

        fun getInstance(remote: FlightDataSource.Remote, local: FlightDataSource.Local) =
            instance ?: FlightRepository(remote, local).also { instance = it }
    }
}
