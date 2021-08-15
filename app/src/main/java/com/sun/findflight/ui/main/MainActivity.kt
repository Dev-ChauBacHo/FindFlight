package com.sun.findflight.ui.main

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import com.google.android.material.navigation.NavigationBarView
import com.sun.findflight.R
import com.sun.findflight.base.BaseActivity
import com.sun.findflight.databinding.ActivityMainBinding
import com.sun.findflight.ui.discover.DiscoverFragment
import com.sun.findflight.ui.favourite.FlightsFavouriteFragment
import com.sun.findflight.ui.home.HomeFragment
import com.sun.findflight.utils.hide
import com.sun.findflight.utils.show

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    override val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var mainMenu: Menu? = null
    private val onBottomNavigation = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.menuHome -> {
                setBackButtonStatus(false)
                openFragment(HomeFragment())
            }
            R.id.menuFavorite -> {
                setBackButtonStatus(false)
                openFragment(FlightsFavouriteFragment())
            }
            R.id.menuDiscover -> {
                setBackButtonStatus(false)
                openFragment(DiscoverFragment())
            }
        }
        true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mainMenu = menu
        hideSearchMenu()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initComponents() {
        with(viewBinding) {
            fabHome.setOnClickListener(this@MainActivity)
            bottomNavigationView.apply {
                setOnItemSelectedListener(onBottomNavigation)
                background = null
                selectedItemId = R.id.menuHome
            }
        }
    }

    override fun onClick(v: View?) = with(viewBinding) {
        when (v) {
            fabHome -> bottomNavigationView.selectedItemId = R.id.menuHome
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == EMPTY_BACKSTACK) super.onBackPressed()
        else supportFragmentManager.popBackStack()
    }

    fun setBackButtonStatus(status: Boolean) = supportActionBar?.setDisplayHomeAsUpEnabled(status)

    fun getSearchView(): SearchView? {
        val item = mainMenu?.findItem(R.id.menuSearch)
        item?.isVisible = true
//        mainMenu?.findItem(R.id.menuReminder)?.isVisible = false
        return item?.let { it.actionView as SearchView }
    }

    fun hideSearchMenu() {
//        mainMenu?.findItem(R.id.menuReminder)?.isVisible = true
        mainMenu?.findItem(R.id.menuSearch)?.isVisible = false
    }

    //TODO change Title of each screen
    fun changeTitle(message: String) {
        title = message
    }

    fun setBottomNavigationVisibility(visibility: Boolean) = with (viewBinding) {
        if (visibility) {
            bottomAppBar.show()
            fabHome.show()
        } else {
            bottomAppBar.hide()
            fabHome.hide()
        }
    }

    companion object {
        const val EMPTY_BACKSTACK = 0

        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
