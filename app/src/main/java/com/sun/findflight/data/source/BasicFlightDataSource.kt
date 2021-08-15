package com.sun.findflight.data.source

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.source.ultils.OnDataCallBack

interface BasicFlightDataSource {
    interface Local {
        fun getBasicFlight(callBack: OnDataCallBack<BasicFlight>)
        fun updateBasicFlight(basicFlight: BasicFlight, callBack: OnDataCallBack<Boolean>)
    }
}
