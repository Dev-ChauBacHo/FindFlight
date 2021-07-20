package com.sun.findflight.ui.splash

import com.sun.findflight.base.BasePresenter
import com.sun.findflight.base.BaseView

interface SplashContract {
    interface View : BaseView {
        fun notifyTokenUpdated()
    }

    interface Presenter : BasePresenter {
        fun updateToken()
    }
}
