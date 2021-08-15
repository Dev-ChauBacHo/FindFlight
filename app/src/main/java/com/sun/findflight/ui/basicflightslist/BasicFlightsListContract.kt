package com.sun.findflight.ui.basicflightslist

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight

interface BasicFlightsListContract {
    interface View : BaseView {
        fun showFlights(flights: List<Flight>)
    }

    interface Presenter : BasePresenter {
        fun getFlights(basicFlight: BasicFlight)
        fun insertFlightFavourite(flight: Flight)
        fun deleteFlightFavourite(flight: Flight)
    }
}
