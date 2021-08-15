package com.sun.findflight.data.source

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.source.ultils.OnDataCallBack

interface FlightDataSource {

    interface Local {
        fun getLocalFlights(callBack: OnDataCallBack<List<Flight>>)
        fun insertFlight(flight: Flight, callBack: OnDataCallBack<String>)
        fun deleteFlight(flightId: String, callBack: OnDataCallBack<Boolean>)
//        fun isFavorite(flight: Flight, callBack: OnDataCallBack<String>)
        fun isFavorite(flights: List<Flight>, callBack: OnDataCallBack<Unit>)
    }

    interface Remote {
        fun getFlights(basicFlight: BasicFlight, callBack: OnDataCallBack<List<Flight>>)
        fun getFlightsName(basicFlight: BasicFlight, callBack: OnDataCallBack<String>)
    }
}
