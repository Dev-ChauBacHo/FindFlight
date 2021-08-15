package com.sun.findflight.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.sun.findflight.R

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    protected abstract val viewBinding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initComponents()
    }

    protected fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(R.id.frameMain, fragment)
            .commit()
    }

    protected abstract fun initComponents()

    private fun fragmentIsVisible(tag: String): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return fragment != null && fragment.isVisible
    }
}
