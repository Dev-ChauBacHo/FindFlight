package com.sun.findflight.ui.topplacefragment

import com.sun.findflight.R
import com.sun.findflight.data.model.Place
import com.sun.findflight.data.repository.PlaceRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack

class TopPlacePresenter(
    private val view: TopPlaceContract.View,
    private val repository: PlaceRepository
) : TopPlaceContract.Presenter {

    override fun getPlaces(originCode: String, month: String) {
        view.showLoading()
        repository.searchTopPlace(originCode, month, object : OnDataCallBack<List<Place>> {
            override fun onSuccess(data: List<Place>) = dataAvailable(data)

            override fun onFailure(e: Exception?) = dataUnavailable()
        })
    }

    private fun dataAvailable(data: List<Place>) = try {
        if (data.isEmpty()) view.showMessage(R.string.error_cant_find_place)
        view.hideLoading()
        view.showPlaces(data)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }

    private fun dataUnavailable() = try {
        view.showMessage(R.string.error_cant_find_place)
        view.hideLoading()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun getData() {

    }
}
