package com.sun.findflight.data.source.local.dao

import android.annotation.SuppressLint
import android.database.Cursor
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_DEPARTURE_DATE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_DESTINATION_CODE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_FLIGHT_LINK
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_ID
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_ORIGIN_CODE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_RETURN_DATE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_TABLE_NAME
import com.sun.findflight.data.source.local.db.AppDatabase

class FlightDaoImpl private constructor(database: AppDatabase) : FlightDao {

    private val writeableDB = database.writableDatabase
    private val readableDB = database.readableDatabase

    override fun getLocalFlights(): List<Flight> {
        val flights = mutableListOf<Flight>()
        val cursor = readableDB.query(
            FLIGHT_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                flights.add(Flight(it))
            }
        }
        return flights
    }

    override fun insertFlight(flight: Flight): String {
        flight.isFavourite = FAVOURITE
        return writeableDB.insert(FLIGHT_TABLE_NAME, null, flight.getContentValue()).toString()
    }

    override fun deleteFlight(flightId: String) =
        writeableDB.delete(FLIGHT_TABLE_NAME, "${FLIGHT_ID}=?", arrayOf(flightId)) > 0

    @SuppressLint("Recycle")
    override fun isFavourite(flights: List<Flight>) {
        val whereClause = "$FLIGHT_ORIGIN_CODE = ? " +
                "AND $FLIGHT_DESTINATION_CODE = ? " +
                "AND $FLIGHT_DEPARTURE_DATE = ? " +
                "AND $FLIGHT_RETURN_DATE = ? " +
                "AND $FLIGHT_FLIGHT_LINK = ?"
        var whereArgs: Array<String>
        var cursor: Cursor
        flights.forEach {
            whereArgs = arrayOf(
                it.originCode,
                it.destinationCode,
                it.departureDate,
                it.returnDate,
                it.flightLink
            )
            cursor = readableDB.query(
                FLIGHT_TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                it.id = Flight(cursor).id
                it.isFavourite = FAVOURITE
            } else {
                it.id = DATA_NOT_VALID
            }
        }
    }

    companion object {
        const val FAVOURITE = 1
        const val DATA_NOT_VALID = "-1"
        private var instance: FlightDaoImpl? = null

        fun getInstance(database: AppDatabase) =
            instance ?: FlightDaoImpl(database).also { instance = it }
    }
}
