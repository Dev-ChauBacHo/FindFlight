package com.sun.findflight.ui.favourite

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight

interface FlightsFavouriteContract {
    interface View : BaseView {
        fun showFlights(flights: List<Flight>)
        fun flightDeleted(flight: Flight)
    }

    interface Presenter : BasePresenter {
        fun getLocalFlights()
        fun deleteFlightFavourite(flight: Flight)
    }
}
