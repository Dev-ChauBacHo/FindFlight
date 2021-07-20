package com.sun.findflight.data.repository

import com.sun.findflight.data.source.TokenDataSource
import com.sun.findflight.data.source.ultils.OnDataCallBack

class TokenRepository private constructor(
    private val remote: TokenDataSource.Remote
) : TokenDataSource.Remote {

    override fun updateToken(callback: OnDataCallBack<Unit>) {
        remote.updateToken(callback)
    }

    companion object {
        private var instance: TokenRepository? = null

        fun getInstance(remote: TokenDataSource.Remote) =
            instance ?: TokenRepository(remote).also { instance = it }
    }
}
