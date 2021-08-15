package com.sun.findflight.data.source

import com.sun.findflight.data.model.Place
import com.sun.findflight.data.source.ultils.OnDataCallBack

interface PlaceDataSource {
    interface Remote {
        fun searchPlace(keyword: String, callBack: OnDataCallBack<List<Place>>)

        fun searchNearestAirport(
            latitude: String,
            longitude: String,
            callBack: OnDataCallBack<List<Place>>
        )

        fun searchTopPlace(originCode: String, month: String, callBack: OnDataCallBack<List<Place>>)
    }
}
