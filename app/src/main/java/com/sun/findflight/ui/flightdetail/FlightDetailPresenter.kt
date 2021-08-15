package com.sun.findflight.ui.flightdetail

import com.sun.findflight.R
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.repository.FlightInfoRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack

class FlightDetailPresenter(
    private val view: FlightDetailContract.View,
    private val repository: FlightInfoRepository
) : FlightDetailContract.Presenter {

    override fun updateFlight(flightDetail: FlightDetail) {
        try {
            repository.updateFlight(flightDetail, object : OnDataCallBack<Boolean> {
                override fun onSuccess(data: Boolean) {
                    view.showFlight(flightDetail)
                    view.hideLoading()
                    if (!data) view.showMessage(R.string.text_fields_not_available)
                }

                override fun onFailure(e: Exception?) {
                    view.showFlight(flightDetail)
                    view.showMessage(R.string.text_fields_not_available)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
