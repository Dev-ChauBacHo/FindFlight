package com.sun.findflight.data.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import androidx.core.database.getStringOrNull
import com.sun.findflight.utils.BaseConst
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class Flight(
    var id: String = "1",
    val originCode: String,
    val destinationCode: String,
    var originName: String? = null,
    var destinationName: String? = null,
    val departureDate: String,
    val returnDate: String,
    val flightLink: String,
    var isFavourite: Int = 0
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        originCode = jsonObject.getString(BaseConst.ORIGIN),
        destinationCode = jsonObject.getString(BaseConst.DESTINATION),
        departureDate = jsonObject.getString(BaseConst.DEPARTURE_DATE),
        returnDate = jsonObject.getString(BaseConst.RETURN_DATE),
        flightLink = jsonObject.getJSONObject(BaseConst.LINK).getString(BaseConst.FLIGHT_OFFERS),
    )

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(FLIGHT_ID)),
        cursor.getString(cursor.getColumnIndex(FLIGHT_ORIGIN_CODE)),
        cursor.getString(cursor.getColumnIndex(FLIGHT_DESTINATION_CODE)),
        cursor.getStringOrNull(cursor.getColumnIndex(FLIGHT_ORIGIN_NAME)),
        cursor.getStringOrNull(cursor.getColumnIndex(FLIGHT_DESTINATION_NAME)),
        cursor.getString(cursor.getColumnIndex(FLIGHT_DEPARTURE_DATE)),
        cursor.getString(cursor.getColumnIndex(FLIGHT_RETURN_DATE)),
        cursor.getString(cursor.getColumnIndex(FLIGHT_FLIGHT_LINK)),
        cursor.getInt(cursor.getColumnIndex(FLIGHT_IS_FAVOURITE))
    )

    fun getContentValue() = ContentValues().apply {
//        put(FLIGHT_ID, id)
        put(FLIGHT_ORIGIN_CODE, originCode)
        put(FLIGHT_DESTINATION_CODE, destinationCode)
        put(FLIGHT_ORIGIN_NAME, originName)
        put(FLIGHT_DESTINATION_NAME, destinationName)
        put(FLIGHT_DEPARTURE_DATE, departureDate)
        put(FLIGHT_RETURN_DATE, returnDate)
        put(FLIGHT_FLIGHT_LINK, flightLink)
        put(FLIGHT_IS_FAVOURITE, isFavourite)
    }

    companion object {
        const val FLIGHT_TABLE_NAME = "flight"
        const val FLIGHT_ID = "id"
        const val FLIGHT_ORIGIN_CODE = "originCode"
        const val FLIGHT_DESTINATION_CODE = "destinationCode"
        const val FLIGHT_ORIGIN_NAME = "originName"
        const val FLIGHT_DESTINATION_NAME = "destinationName"
        const val FLIGHT_DEPARTURE_DATE = "departureDate"
        const val FLIGHT_RETURN_DATE = "returnDate"
        const val FLIGHT_FLIGHT_LINK = "flightLink"
        const val FLIGHT_IS_FAVOURITE = "isFavourite"
    }
}
