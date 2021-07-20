package com.sun.findflight.data.source.remote

import com.sun.findflight.data.source.TokenDataSource
import com.sun.findflight.data.source.ultils.DownloaderTask
import com.sun.findflight.data.source.ultils.OnDataCallBack
import com.sun.findflight.utils.requestNewToken
import java.util.concurrent.TimeUnit

class TokenRemoteDataSource private constructor() : TokenDataSource.Remote {

    override fun updateToken(callback: OnDataCallBack<Unit>) {
        DownloaderTask<Unit, Unit>(callback) {
            requestNewToken()
        }.execute(Unit).get(DownloaderTask.WAIT_TIMEOUT, TimeUnit.MILLISECONDS)
    }

    companion object {
        private var instance: TokenRemoteDataSource? = null

        fun getInstance() = instance ?: TokenRemoteDataSource().also { instance = it }
    }
}
