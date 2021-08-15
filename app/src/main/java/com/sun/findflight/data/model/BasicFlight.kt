package com.sun.findflight.data.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import androidx.core.database.getStringOrNull
import com.sun.findflight.ui.home.HomeFragment
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasicFlight(
    val id: String = "1",
    val originCode: String,
    var destinationCode: String? = null,
    val originName: String? = null,
    var destinationName: String? = null,
    var departureDate: String? = null,
    val oneWay: Boolean = false,
    var returnDate: String? = null,
    val adult: String = HomeFragment.DEFAULT_ADULT_SEAT,
    val child: String = HomeFragment.NO_PASSENGER,
    val infant: String = HomeFragment.NO_PASSENGER,
    val travelClass: String,
    val currencyCode: String,
) : Parcelable {

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_ID)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_ORIGIN_CODE)),
        cursor.getStringOrNull(cursor.getColumnIndex(BASIC_FLIGHT_DESTINATION_CODE)),
        cursor.getStringOrNull(cursor.getColumnIndex(BASIC_FLIGHT_ORIGIN_NAME)),
        cursor.getStringOrNull(cursor.getColumnIndex(BASIC_FLIGHT_DESTINATION_NAME)),
        cursor.getStringOrNull(cursor.getColumnIndex(BASIC_FLIGHT_DEPARTURE_DATE)),
        cursor.getInt(cursor.getColumnIndex(BASIC_FLIGHT_ONEWAY)) != 0,
        cursor.getStringOrNull(cursor.getColumnIndex(BASIC_FLIGHT_RETURN_DATE)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_ADULT)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_CHILD)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_INFANT)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_TRAVEL_CLASS)),
        cursor.getString(cursor.getColumnIndex(BASIC_FLIGHT_CURRENCY_CODE)),
    )

    fun getContentValue() = ContentValues().apply {
        put(BASIC_FLIGHT_ID, id)
        put(BASIC_FLIGHT_ORIGIN_CODE, originCode)
        put(BASIC_FLIGHT_DESTINATION_CODE, destinationCode)
        put(BASIC_FLIGHT_ORIGIN_NAME, originName)
        put(BASIC_FLIGHT_DESTINATION_NAME, destinationName)
        put(BASIC_FLIGHT_DEPARTURE_DATE, departureDate)
        put(BASIC_FLIGHT_ONEWAY, oneWay)
        put(BASIC_FLIGHT_RETURN_DATE, returnDate)
        put(BASIC_FLIGHT_ADULT, adult)
        put(BASIC_FLIGHT_CHILD, child)
        put(BASIC_FLIGHT_INFANT, infant)
        put(BASIC_FLIGHT_TRAVEL_CLASS, travelClass)
        put(BASIC_FLIGHT_CURRENCY_CODE, currencyCode)
    }

    companion object {
        const val BASIC_FLIGHT_TABLE_NAME = "basic_flight"
        const val BASIC_FLIGHT_ID = "id"
        const val BASIC_FLIGHT_ORIGIN_CODE = "originCode"
        const val BASIC_FLIGHT_DESTINATION_CODE = "destinationCode"
        const val BASIC_FLIGHT_ORIGIN_NAME = "originName"
        const val BASIC_FLIGHT_DESTINATION_NAME = "destinationName"
        const val BASIC_FLIGHT_DEPARTURE_DATE = "departureDate"
        const val BASIC_FLIGHT_ONEWAY = "oneWay"
        const val BASIC_FLIGHT_RETURN_DATE = "returnDate"
        const val BASIC_FLIGHT_ADULT = "adult"
        const val BASIC_FLIGHT_CHILD = "child"
        const val BASIC_FLIGHT_INFANT = "infant"
        const val BASIC_FLIGHT_TRAVEL_CLASS = "travelClass"
        const val BASIC_FLIGHT_CURRENCY_CODE = "currencyCode"
    }
}
