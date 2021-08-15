package com.sun.findflight.data.repository

import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.FlightInfoDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightInfoRepository(
    private val remote: FlightInfoDataSource.Remote
) : FlightInfoDataSource.Remote {

    override fun updateFlight(flightDetail: FlightDetail, callBack: OnDataCallBack<Boolean>) {
        remote.updateFlight(flightDetail, callBack)
    }

    companion object {
        private var instance: FlightInfoRepository? = null

        fun getInstance(remote: FlightInfoDataSource.Remote) =
            instance ?: FlightInfoRepository(remote).also { instance = it }
    }
}
