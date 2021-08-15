package com.sun.findflight.ui.searchPlace

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView
import com.sun.findflight.data.model.Place

interface SearchPlaceContract {
    interface View : BaseView {
        fun showPlaces(listPlace: List<Place>)
    }

    interface Presenter : BasePresenter {
        fun getPlaces(keyword: String)
        fun getNearestPlaces(latitude: String, longitude: String)
    }
}
