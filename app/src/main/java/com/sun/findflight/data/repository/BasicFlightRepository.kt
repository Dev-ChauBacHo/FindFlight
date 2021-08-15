package com.sun.findflight.data.repository

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.source.BasicFlightDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class BasicFlightRepository private constructor(
    private val local: BasicFlightDataSource.Local
) : BasicFlightDataSource.Local {

    override fun getBasicFlight(callBack: OnDataCallBack<BasicFlight>) {
        local.getBasicFlight(callBack)
    }

    override fun updateBasicFlight(basicFlight: BasicFlight, callBack: OnDataCallBack<Boolean>) {
        local.updateBasicFlight(basicFlight, callBack)
    }

    companion object {
        private var instance: BasicFlightRepository? = null

        fun getInstance(local: BasicFlightDataSource.Local) =
            instance ?: BasicFlightRepository(local).also { instance = it }
    }
}
