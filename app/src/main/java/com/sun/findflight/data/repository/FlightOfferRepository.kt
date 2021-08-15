package com.sun.findflight.data.repository

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.FlightOfferDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightOfferRepository private constructor(
    private val remote: FlightOfferDataSource.Remote
) : FlightOfferDataSource.Remote {

    override fun getOfferFlights(
        basicFlight: BasicFlight,
        callBack: OnDataCallBack<List<FlightDetail>>
    ) {
        remote.getOfferFlights(basicFlight, callBack)
    }

    companion object {
        private var instance: FlightOfferRepository? = null

        fun getInstance(remote: FlightOfferDataSource.Remote) =
            instance ?: FlightOfferRepository((remote)).also { instance = it }
    }
}
