package com.sun.findflight.base

import androidx.annotation.StringRes

interface BaseView {
    fun showMessage(@StringRes messageRes: Int)
    fun showLoading()
    fun hideLoading()
}
