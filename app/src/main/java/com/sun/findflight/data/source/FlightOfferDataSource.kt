package com.sun.findflight.data.source

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.ultils.OnDataCallBack

interface FlightOfferDataSource {

    interface Remote {
        fun getOfferFlights(basicFlight: BasicFlight, callBack: OnDataCallBack<List<FlightDetail>>)
    }
}
