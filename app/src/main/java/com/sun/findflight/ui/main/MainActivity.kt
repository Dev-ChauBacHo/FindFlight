package com.sun.findflight.ui.main

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.sun.findflight.R
import com.sun.findflight.base.BaseActivity
import com.sun.findflight.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val onBottomNavigation : NavigationBarView.OnItemSelectedListener? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initComponents() {
        viewBinding.bottomNavigationView.apply {
            setOnItemSelectedListener(onBottomNavigation)
            selectedItemId = R.id.menuHome
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
