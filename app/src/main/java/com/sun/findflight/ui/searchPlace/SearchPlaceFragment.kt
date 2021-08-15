package com.sun.findflight.ui.searchPlace

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.Place
import com.sun.findflight.databinding.FragmentSearchPlaceBinding
import com.sun.findflight.ui.discover.DiscoverFragment
import com.sun.findflight.ui.home.HomeFragment
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.searchPlace.adapter.PlaceListAdapter
import com.sun.findflight.utils.*

class SearchPlaceFragment : BaseFragment<FragmentSearchPlaceBinding>(), SearchPlaceContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchPlaceBinding =
        FragmentSearchPlaceBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showMessage(R.string.text_permission_granted)
            } else {
                showMessage(R.string.text_permission_denied)
            }
        }
    private var presenter: SearchPlacePresenter? = null
    private val placeListAdapter by lazy { PlaceListAdapter(this::itemPlaceClick, mutableListOf()) }
    private var searchView: SearchView? = null
    private var locationListener: LocationListener? = null
    private var locationManager: LocationManager? = null

    override fun initComponents() {
        viewBinding.recyclerPlaces.adapter = placeListAdapter
        with(containerContext) {
            setBackButtonStatus(true)
            searchView = getSearchView().also {
                it?.isIconified = false
                it?.queryHint = resources.getString(R.string.title_search)
            }
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        hideLoading()
    }

    override fun initEvents() {
        viewBinding.buttonGetCurrentPosition.setOnClickListener {
            requestPermission()
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { getPlaces(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?) = true
        })

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                getNearestPlaces(location.latitude.toString(), location.longitude.toString())
                locationManager?.removeUpdates(this)
            }

            override fun onProviderDisabled(provider: String) {
//                super.onProviderDisabled(provider)
                //TODO change in main project
                showMessage(R.string.text_turn_on_gps)
            }
        }

        viewBinding.recyclerPlaces.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    containerContext.setBottomNavigationVisibility(false)
                } else {
                    containerContext.setBottomNavigationVisibility(true)
                }
            }
        })
    }

    override fun initData() {
        val repository = RepositoryUtils.getPlaceRepository()
        presenter = SearchPlacePresenter(this, repository)
    }

    override fun showPlaces(listPlace: List<Place>) {
        placeListAdapter.updateData(listPlace.toMutableList())
    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {
        placeListAdapter.updateData(mutableListOf())
        viewBinding.progressBarPlace.show()
        closeKeyboard()
    }

    override fun hideLoading() {
        viewBinding.progressBarPlace.hide()
    }

    private fun getPlaces(keyword: String) {
        presenter?.getPlaces(keyword)
    }

    private fun getNearestPlaces(latitude: String, longitude: String) {
        presenter?.getNearestPlaces(latitude, longitude)
    }

    private fun itemPlaceClick(place: Place) {
        setResult(place)
    }

    private fun setResult(place: Place) {
        val bundle = bundleOf(HomeFragment.KEY_DATA to place)
        when (this.tag) {
            HomeFragment.KEY_PLACE_FROM -> {
                parentFragmentManager.setFragmentResult(
                    HomeFragment.KEY_PLACE_FROM,
                    bundle
                )
            }
            HomeFragment.KEY_PLACE_TO -> {
                parentFragmentManager.setFragmentResult(
                    HomeFragment.KEY_PLACE_TO,
                    bundle
                )
            }
            DiscoverFragment.KEY_DISCOVER -> {
                parentFragmentManager.setFragmentResult(
                    DiscoverFragment.KEY_DISCOVER,
                    bundle
                )
            }
        }
        containerContext.onBackPressed()
    }

    private fun requestPermission() {
        when {
            containerContext.checkSelfPermission(LOCATION_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED -> {
                locationListener?.let {
                    locationManager?.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_UPDATE_TIME,
                        MIN_UPDATE_DISTANCE,
                        it
                    )
                }
            }
            shouldShowRequestPermissionRationale(LOCATION_PERMISSION) -> {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                        getString(R.string.text_package),
                        context?.packageName,
                        null
                    )
                )
                context?.showExplainDialog(intent)
            }
            else -> getPermission.launch(LOCATION_PERMISSION)
        }
    }

    override fun onDetach() {
        searchView?.isIconified = true
        with(containerContext) {
            hideSearchMenu()
            setBackButtonStatus(false)
            setBottomNavigationVisibility(true)
        }
        //TODO change in main project
        closeKeyboard()
        //TODO hide appbar on MainActivity
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationListener?.let { locationManager?.removeUpdates(it) }
    }

    companion object {
        private const val TAG = "SearchPlaceFragment"
        const val FLAG_OPERATING = 0
        const val MIN_UPDATE_TIME = 100L
        const val MIN_UPDATE_DISTANCE = 0F
        const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}
