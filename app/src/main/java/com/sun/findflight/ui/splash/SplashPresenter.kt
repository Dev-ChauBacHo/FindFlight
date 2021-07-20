package com.sun.findflight.ui.splash

import android.content.res.Resources
import com.sun.findflight.R
import com.sun.findflight.data.repository.TokenRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack

class SplashPresenter(
    private val view: SplashContract.View,
    private val repository: TokenRepository,
) : SplashContract.Presenter {

    override fun updateToken() {
        repository.updateToken(object : OnDataCallBack<Unit> {
            override fun onSuccess(data: Unit) {
                view.notifyTokenUpdated()
            }

            override fun onFailure(e: Exception?) {
                view.showMessage(ERROR_GET_TOKEN)
            }

        })
    }

    override fun getData() {
        updateToken()
    }

    companion object {
        const val ERROR_GET_TOKEN = "Error! Can only use offline function!"
    }
}
