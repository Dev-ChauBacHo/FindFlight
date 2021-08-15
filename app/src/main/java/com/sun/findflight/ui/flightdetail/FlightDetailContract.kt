package com.sun.findflight.ui.flightdetail

import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.FlightDetail

interface FlightDetailContract {

    interface View : BaseView {
        fun showFlight(flightDetail: FlightDetail)
    }

    interface Presenter {
        fun updateFlight(flightDetail: FlightDetail)
    }
}
