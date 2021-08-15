package com.sun.findflight.data.source.local.dao

import com.sun.findflight.data.model.Flight

interface FlightDao {
    fun getLocalFlights(): List<Flight>
    fun insertFlight(flight: Flight): String
    fun deleteFlight(flightId: String): Boolean
    //    fun isFavourite(flight: Flight): String
    fun isFavourite(flights: List<Flight>)
}
