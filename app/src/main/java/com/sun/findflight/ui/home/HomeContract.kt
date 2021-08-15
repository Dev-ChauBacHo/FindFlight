package com.sun.findflight.ui.home

import com.sun.findflight.data.model.BasicFlight

interface HomeContract {

    interface View {
        fun showBasicFlight(basicFlight: BasicFlight)
    }

    interface Presenter {
        fun getBasicFlight()
        fun updateBasicFlight(basicFlight: BasicFlight)
    }
}
