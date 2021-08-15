package com.sun.findflight.data.model

import android.os.Parcelable
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.SegmentModelConst
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject

@Parcelize
data class Landing(
    val placeCode: String,
    val terminal: String?,
    val time: String,
    var timezone: String? = null,
    var name: String? = null,
    var countryCode: String? = null
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        placeCode = jsonObject.getString(BaseConst.IATA_CODE),
        terminal = try {
            jsonObject.getString(SegmentModelConst.TERMINAl)
        } catch (e: JSONException) {
            null
        },
        time = jsonObject.getString(SegmentModelConst.AT_TIME),
    )
}
