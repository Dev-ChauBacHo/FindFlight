package com.sun.findflight.data.model

import android.os.Parcelable
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.FlightModelConst
import com.sun.findflight.utils.parseJsonArrayToObjects
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class FlightDetail(
    val lastTicketingDate: String,
    val oneWay: Boolean,
    val numberOfBookableSeats: String,
    val currency: Currency,
    val airlinesCode: List<String>,
    val duration: String,
    val segments: List<Segment>,
    val travelers: List<Traveler>,
    var basicFlight: BasicFlight? = null,
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        lastTicketingDate = jsonObject.getString(FlightModelConst.LAST_TICKETING_DATE),
        oneWay = jsonObject.getBoolean(FlightModelConst.ONEWAY),
        numberOfBookableSeats = jsonObject.getString(FlightModelConst.BOOKABLE_SEATS),
        currency = Currency(jsonObject.getJSONObject(BaseConst.PRICE)),
        airlinesCode = jsonObject.getString(FlightModelConst.VALIDATE_AIRLINE)
            .parseJsonArrayToObjects(),
        duration = jsonObject.getJSONArray(FlightModelConst.ITINERARIES)
            .getJSONObject(FlightModelConst.INDEX_ZERO)
            .getString(FlightModelConst.DURATION),
        segments = jsonObject.getJSONArray(FlightModelConst.ITINERARIES)
            .getJSONObject(FlightModelConst.INDEX_ZERO)
            .getString(FlightModelConst.SEGMENTS)
            .parseJsonArrayToObjects(),
        travelers = jsonObject.getString(FlightModelConst.TRAVELER_PRICING)
            .parseJsonArrayToObjects(),
    )
}
