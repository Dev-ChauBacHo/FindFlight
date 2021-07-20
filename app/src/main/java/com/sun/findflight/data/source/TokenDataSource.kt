package com.sun.findflight.data.source

import com.sun.findflight.data.source.ultils.OnDataCallBack

interface TokenDataSource {

    interface Remote {
        fun updateToken(callback: OnDataCallBack<Unit>)
    }
}
