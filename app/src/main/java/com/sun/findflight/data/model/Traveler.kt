package com.sun.findflight.data.model

import android.os.Parcelable
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.TravelerModelConst
import com.sun.findflight.utils.parseJsonArrayToObjects
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class Traveler(
    val fareOption: String,
    val travelerType: String,
    val price: String,
    val segmentFareDetail: List<SegmentFareDetail>,
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        fareOption = jsonObject.getString(TravelerModelConst.FARE_OPTION),
        travelerType = jsonObject.getString(TravelerModelConst.TRAVELER_TYPE),
        price = jsonObject.getJSONObject(BaseConst.PRICE).getString(BaseConst.TOTAL),
        segmentFareDetail = jsonObject.getString(TravelerModelConst.FARE_DETAILS)
            .parseJsonArrayToObjects(),
    )
}
