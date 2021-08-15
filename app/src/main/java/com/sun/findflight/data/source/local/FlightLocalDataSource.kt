package com.sun.findflight.data.source.local

import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.source.FlightDataSource
import com.sun.findflight.data.source.local.dao.FlightDao
import com.sun.findflight.data.source.ultils.DownloaderTask
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightLocalDataSource private constructor(
    private val flightDao: FlightDao
) : FlightDataSource.Local {

    override fun getLocalFlights(callBack: OnDataCallBack<List<Flight>>) {
        DownloaderTask<Unit, List<Flight>>(callBack) {
            flightDao.getLocalFlights()
        }.execute(Unit)
    }

    override fun insertFlight(flight: Flight, callBack: OnDataCallBack<String>) {
        DownloaderTask<Flight, String>(callBack) {
            flightDao.insertFlight(flight)
        }.execute(flight)
    }

    override fun deleteFlight(flightId: String, callBack: OnDataCallBack<Boolean>) {
        DownloaderTask<String, Boolean>(callBack) {
            flightDao.deleteFlight(flightId)
        }.execute(flightId)
    }

    override fun isFavorite(flights: List<Flight>, callBack: OnDataCallBack<Unit>) {
        DownloaderTask<List<Flight>, Unit>(callBack) {
            flightDao.isFavourite(flights)
        }.execute(flights)
    }

    companion object {
        private var instance: FlightLocalDataSource? = null

        fun getInstance(flightDao: FlightDao) =
            instance ?: FlightLocalDataSource(flightDao).also { instance = it }
    }
}
