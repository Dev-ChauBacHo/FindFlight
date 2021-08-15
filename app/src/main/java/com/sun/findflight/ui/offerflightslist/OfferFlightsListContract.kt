package com.sun.findflight.ui.offerflightslist

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail

interface OfferFlightsListContract {
    interface View : BaseView {
        fun showOfferFlights(flights: List<FlightDetail>)
    }

    interface Presenter : BasePresenter {
        fun getOfferFlights(basicFlight: BasicFlight)
    }
}
