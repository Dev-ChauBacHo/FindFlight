package com.sun.findflight.ui.splash

import android.os.Handler
import android.os.Looper
import com.sun.findflight.base.BaseActivity
import com.sun.findflight.databinding.ActivitySplashBinding
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.utils.RepositoryUtils
import com.sun.findflight.utils.showToast

private const val TIME_WAITING = 500L

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashContract.View {

    private var splashPresenter: SplashPresenter? = null

    override val viewBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun initComponents() {
        initPresenter()
        Handler(Looper.getMainLooper()).postDelayed({
            splashPresenter?.getData()
        }, TIME_WAITING)
    }

    override fun notifyTokenUpdated() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    override fun showMessage(data: Any) {
        showToast(data.toString())
        notifyTokenUpdated()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun initPresenter() {
        val repository = RepositoryUtils.getTokenRepository()
        splashPresenter = SplashPresenter(this, repository)
    }

}
