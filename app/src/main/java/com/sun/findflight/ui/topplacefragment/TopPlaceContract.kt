package com.sun.findflight.ui.topplacefragment

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.Place

interface TopPlaceContract {
    interface View : BaseView {
        fun showPlaces(listPlace: List<Place>)
    }

    interface Presenter : BasePresenter {
        fun getPlaces(originCode: String, month: String)
    }
}
