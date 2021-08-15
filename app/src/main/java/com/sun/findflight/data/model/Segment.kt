package com.sun.findflight.data.model

import android.os.Parcelable
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.SegmentModelConst
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class Segment(
    val departure: Landing,
    val arrival: Landing,
    val carrierCode: String,
    val carrierNumber: String,
    val aircraftNumber: String,
    var carrierName: String? = null,
    var aircraftName: String? = null,
    var layover: String? = null
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        departure = Landing(jsonObject.getJSONObject(SegmentModelConst.DEPARTURE)),
        arrival = Landing(jsonObject.getJSONObject(SegmentModelConst.ARRIVAL)),
        carrierCode = jsonObject.getString(BaseConst.CARRIER_CODE),
        carrierNumber = jsonObject.getString(SegmentModelConst.CARRIER_NUMBER),
        aircraftNumber = jsonObject.getJSONObject(SegmentModelConst.AIRCRAFT)
            .getString(SegmentModelConst.AIRCRAFT_CODE),
    )
}
