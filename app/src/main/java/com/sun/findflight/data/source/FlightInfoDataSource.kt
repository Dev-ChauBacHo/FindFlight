package com.sun.findflight.data.source

import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.ultils.OnDataCallBack

interface FlightInfoDataSource {
    interface Remote {
        fun updateFlight(flightDetail: FlightDetail, callBack: OnDataCallBack<Boolean>)
    }
}
