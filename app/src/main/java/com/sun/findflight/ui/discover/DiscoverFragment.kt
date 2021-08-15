package com.sun.findflight.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sun.findflight.R
import com.sun.findflight.base.BaseFragment
import com.sun.findflight.data.model.Place
import com.sun.findflight.databinding.FragmentDiscoverBinding
import com.sun.findflight.ui.home.HomeFragment.Companion.KEY_DATA
import com.sun.findflight.ui.main.MainActivity
import com.sun.findflight.ui.searchPlace.SearchPlaceFragment
import com.sun.findflight.ui.topplacefragment.TopPlaceFragment
import com.sun.findflight.utils.addFragment

class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDiscoverBinding =
        FragmentDiscoverBinding::inflate
    private val containerContext by lazy { context as MainActivity }
    private var yourPlace: Place? = null

    override fun initComponents() {
        containerContext.changeTitle(getString(R.string.title_discover))
    }

    override fun initEvents() = with(viewBinding) {
        textYourOrigin.setOnClickListener {
            textYourOrigin.setTextColor(resources.getColor(R.color.color_pickled_bluewood, null))
            parentFragmentManager.addFragment(
                R.id.frameMain,
                SearchPlaceFragment(),
                KEY_DISCOVER
            )
        }
        buttonDiscover.setOnClickListener {
            if (placeIsValid()) {
                yourPlace?.let {
                    val month = "${spinnerYear.selectedItem}-${spinnerMonth.selectedItem}"
                    val fragment = TopPlaceFragment()
                    val bundle = Bundle().apply {
                        putString(KEY_DISCOVER_PLACE, it.iataCode)
                        putString(KEY_DISCOVER_MONTH, month)
                    }
                    fragment.arguments = bundle
                    parentFragmentManager.addFragment(R.id.frameMain, fragment)
                }
            }
        }
        setPlaceListener()
    }

    override fun initData() {

    }

    private fun setPlaceListener() {
        val requestKey = KEY_DISCOVER
        parentFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == requestKey) {
                val result = bundle.getParcelable<Place>(KEY_DATA)
                yourPlace = result
                viewBinding.textYourOrigin.text = result?.detailedName
            }
        }
    }

    private fun placeIsValid(): Boolean {
        with(viewBinding) {
            if (textYourOrigin.text.isEmpty()) {
                textYourOrigin.setTextColor(resources.getColor(R.color.color_sunset_orange, null))
                textYourOrigin.apply {
                    this.setHintTextColor(
                        resources.getColor(
                            R.color.color_sunset_orange,
                            null
                        )
                    )
                    this.hint = context.getString(R.string.text_field_required)
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
                }
                return false
            }
            return true
        }
    }

    companion object {
        const val KEY_DISCOVER = "discover"
        const val KEY_DISCOVER_PLACE = "discover_place"
        const val KEY_DISCOVER_MONTH = "discover_month"
    }
}
