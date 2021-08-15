package com.sun.findflight.data.source.remote

import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.model.Place
import com.sun.findflight.data.source.FlightInfoDataSource
import com.sun.findflight.data.source.remote.api.ApiQuery
import com.sun.findflight.data.source.remote.ultils.getNetworkData
import com.sun.findflight.data.source.ultils.DownloaderTask
import com.sun.findflight.data.source.ultils.OnDataCallBack
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.TimeUtils
import com.sun.findflight.utils.parseJsonArrayToObjects
import org.json.JSONObject

class FlightInfoRemoteDataSource private constructor() : FlightInfoDataSource.Remote {

    override fun updateFlight(flightDetail: FlightDetail, callBack: OnDataCallBack<Boolean>) {
        DownloaderTask<Unit, Boolean>(callBack) {
            updateFlight(flightDetail)
        }.execute(Unit)
    }

    private fun updateFlight(flightDetail: FlightDetail): Boolean {
        with(flightDetail) {
            segments.first().departure.apply {
                name = basicFlight?.originName
                timezone = searchPlace(placeCode).timeZone
            }
            segments.last().arrival.apply {
                name = basicFlight?.destinationName
                timezone = searchPlace(placeCode).timeZone
            }

            for (i in INDEX_FIRST until segments.size) {
                segments[i].departure.apply {
                    val place = searchPlace(placeCode)
                    name = place.detailedName
                    timezone = place.timeZone
                }
            }

            for (i in INDEX_ZERO until segments.size - ONE_ITEM) {
                segments[i].arrival.apply {
                    name = segments[i + ONE_ITEM].departure.name
                    timezone = segments[i + ONE_ITEM].departure.timezone
                }
            }

            if (segments.size > ONE_ITEM) {
                for (i in INDEX_ZERO until segments.size - ONE_ITEM) {
                    TimeUtils.calculateTimeGap(
                        segments[i].arrival.time.replace(
                            OLD_VALUE,
                            NEW_VALUE
                        ),
                        segments[i + ONE_ITEM].departure.time.replace(
                            OLD_VALUE,
                            NEW_VALUE
                        )
                    ).also {
                        if (it.isNotEmpty()) segments[i].layover = it
                    }
                }
            }
        }
        return true
    }

    private fun searchPlace(keyword: String) =
        JSONObject(getNetworkData(ApiQuery.querySearchPlaces(keyword)))
            .getString(BaseConst.DATA)
            .parseJsonArrayToObjects<Place>()
            .first()

    companion object {
        const val INDEX_ZERO = 0
        const val INDEX_FIRST = 1
        const val ONE_ITEM = 1
        const val OLD_VALUE = "T"
        const val NEW_VALUE = " "
        private var instance: FlightInfoRemoteDataSource? = null

        fun getInstance() = instance ?: FlightInfoRemoteDataSource().also { instance = it }
    }
}
