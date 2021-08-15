package com.sun.findflight.ui.home

import android.util.Log
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.repository.BasicFlightRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: BasicFlightRepository
) : HomeContract.Presenter {

    override fun getBasicFlight() {
        repository.getBasicFlight(object : OnDataCallBack<BasicFlight> {
            override fun onSuccess(data: BasicFlight) {
                Log.d(TAG, "onSuccess: $data")
                try {
                    view.showBasicFlight(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(e: Exception?) {
                e?.printStackTrace()
            }
        })
    }

    override fun updateBasicFlight(basicFlight: BasicFlight) {
        Log.d(TAG, "updateBasicFlight: $basicFlight")
        repository.updateBasicFlight(basicFlight, object : OnDataCallBack<Boolean> {
            override fun onSuccess(data: Boolean) {
                Log.d(TAG, "onSuccess: $data")
            }

            override fun onFailure(e: Exception?) {
                e?.printStackTrace()
            }
        })
    }

    companion object {
        private const val TAG = "HomePresenter"
    }
}
