package com.sun.findflight.data.source.local

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.source.BasicFlightDataSource
import com.sun.findflight.data.source.local.dao.BasicFlightDao
import com.sun.findflight.data.source.ultils.DownloaderTask
import com.sun.findflight.data.source.ultils.OnDataCallBack

class BasicFlightLocalDataSource private constructor(
    private val basicFlightDao: BasicFlightDao
) : BasicFlightDataSource.Local {

    override fun getBasicFlight(callBack: OnDataCallBack<BasicFlight>) {
        DownloaderTask<Unit, BasicFlight>(callBack) {
            basicFlightDao.getBasicFlight()
        }.execute(Unit)
    }

    override fun updateBasicFlight(basicFlight: BasicFlight, callBack: OnDataCallBack<Boolean>) {
        DownloaderTask<BasicFlight, Boolean>(callBack) {
            basicFlightDao.updateBasicFlight(basicFlight)
        }.execute(basicFlight)
    }

    companion object {
        private var instance: BasicFlightLocalDataSource? = null

        fun getInstance(basicFlightDao: BasicFlightDao) =
            instance ?: BasicFlightLocalDataSource(basicFlightDao).also { instance = it }
    }
}
