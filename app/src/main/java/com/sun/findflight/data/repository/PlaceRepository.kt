package com.sun.findflight.data.repository

import com.sun.findflight.data.model.Place
import com.sun.findflight.data.source.PlaceDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class PlaceRepository private constructor(
    private val remote: PlaceDataSource.Remote,
) : PlaceDataSource.Remote {

    override fun searchPlace(keyword: String, callBack: OnDataCallBack<List<Place>>) {
        remote.searchPlace(keyword, callBack)
    }

    override fun searchNearestAirport(
        latitude: String,
        longitude: String,
        callBack: OnDataCallBack<List<Place>>
    ) {
        remote.searchNearestAirport(latitude, longitude, callBack)
    }

    override fun searchTopPlace(
        originCode: String,
        month: String,
        callBack: OnDataCallBack<List<Place>>
    ) {
        remote.searchTopPlace(originCode, month, callBack)
    }

    companion object {
        private var instance: PlaceRepository? = null

        fun getInstance(remote: PlaceDataSource.Remote) =
            instance ?: PlaceRepository(remote).also { instance = it }
    }
}
