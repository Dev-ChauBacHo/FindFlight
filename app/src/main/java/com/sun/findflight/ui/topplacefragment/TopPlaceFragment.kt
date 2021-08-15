package com.sun.findflight.ui.topplacefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.Place
import com.sun.findflight.databinding.FragmentTopPlacesBinding
import com.sun.findflight.ui.discover.DiscoverFragment.Companion.KEY_DISCOVER_MONTH
import com.sun.findflight.ui.discover.DiscoverFragment.Companion.KEY_DISCOVER_PLACE
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.searchPlace.adapter.PlaceListAdapter
import com.sun.findflight.utils.*

class TopPlaceFragment : BaseFragment<FragmentTopPlacesBinding>(), TopPlaceContract.View {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopPlacesBinding =
        FragmentTopPlacesBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private var presenter: TopPlacePresenter? = null
    private val topPlaceAdapter by lazy { PlaceListAdapter(this::itemPlaceClick, mutableListOf()) }

    override fun initComponents() {
        viewBinding.recyclerTopPlaces.adapter = topPlaceAdapter
        containerContext.setBackButtonStatus(true)
        hideLoading()
    }

    override fun initEvents() {

    }

    override fun initData() {
        val repository = RepositoryUtils.getPlaceRepository()
        presenter = TopPlacePresenter(this, repository)

        val month = arguments?.getString(KEY_DISCOVER_MONTH)
        val place = arguments?.getString(KEY_DISCOVER_PLACE)

        if (month != null && place != null) presenter?.getPlaces(place, month)
    }

    override fun showPlaces(listPlace: List<Place>) {
        topPlaceAdapter.updateData(listPlace.toMutableList())
    }

    override fun showMessage(messageRes: Int) {
        context?.showToast(resources.getString(messageRes))
    }

    override fun showLoading() {
        topPlaceAdapter.updateData(mutableListOf())
        viewBinding.progressBarTopPlace.show()
        closeKeyboard()
    }

    override fun hideLoading() {
        viewBinding.progressBarTopPlace.hide()
    }

    private fun itemPlaceClick(place: Place) = Unit

    override fun onDetach() {
        containerContext.hideSearchMenu()
        containerContext.setBackButtonStatus(false)
        super.onDetach()
    }
}
