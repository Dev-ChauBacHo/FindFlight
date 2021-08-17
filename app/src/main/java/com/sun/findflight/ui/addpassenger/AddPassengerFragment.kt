package com.sun.findflight.ui.addpassenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import com.sun.findflight.R
import com.sun.findflight.base.BaseSheetDialogFragment
import com.sun.findflight.databinding.FragmentAddPassengerBinding
import com.sun.findflight.ui.home.HomeFragment
import com.sun.findflight.ui.home.HomeFragment.Companion.KEY_ADULT
import com.sun.findflight.ui.home.HomeFragment.Companion.KEY_CHILD
import com.sun.findflight.ui.home.HomeFragment.Companion.KEY_INFANT
import com.sun.findflight.utils.showToast

class AddPassengerFragment : BaseSheetDialogFragment<FragmentAddPassengerBinding>(),
    View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddPassengerBinding =
        FragmentAddPassengerBinding::inflate
    private var total = DEFAULT_TOTAL

    override fun onClick(v: View?) = with(viewBinding) {
        when (v) {
            imageAddAdult -> addPassenger(textPassengerNumberAdult)
            imageMinusAdult -> minusPassenger(textPassengerNumberAdult)
            imageAddChild -> addPassenger(textPassengerNumberChild)
            imageMinusChild -> minusPassenger(textPassengerNumberChild)
            imageAddInfant -> addPassenger(textPassengerNumberInfant)
            imageMinusInfant -> minusPassenger(textPassengerNumberInfant)
            buttonApplyPassenger -> setResult()
        }
    }

    override fun initEvents() {
        viewBinding.run {
            listOf(
                imageMinusAdult, imageAddAdult,
                imageAddChild, imageMinusChild,
                imageAddInfant, imageMinusInfant,
                buttonApplyPassenger
            ).forEach {
                it.setOnClickListener(this@AddPassengerFragment)
            }
        }
    }

    override fun initData() {
        val bundle = this@AddPassengerFragment.arguments
        val adult = bundle?.getString(KEY_ADULT)
        val child = bundle?.getString(KEY_CHILD)
        val infant = bundle?.getString(KEY_INFANT)
        with(viewBinding) {
            adult?.let {
                textPassengerNumberAdult.text = adult
                total = it.toInt()
            }
            child?.let {
                textPassengerNumberChild.text = child
                total += it.toInt()
            }
            infant?.let {
                textPassengerNumberInfant.text = infant
                total += it.toInt()
            }
        }
    }

    private fun addPassenger(textView: TextView) {
        try {
            if (total < MAX_PEOPLE) {
                var value = textView.text.toString().toInt()
                ++value
                textView.text = value.toString()
                ++total
            } else {
                context?.showToast(
                    resources.getString(R.string.error_over_people_number),
                    Toast.LENGTH_SHORT
                )
            }
        } catch (e: NumberFormatException) {
            context?.showToast(resources.getString(R.string.error_common), Toast.LENGTH_SHORT)
        }
    }

    private fun minusPassenger(textView: TextView) {
        try {
            var value = textView.text.toString().toInt()
            if (value > MIN_PEOPLE) {
                --value
                if (textView == viewBinding.textPassengerNumberAdult && value == MIN_PEOPLE) {
                    context?.showToast(
                        resources.getString(R.string.error_below_adult_number),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    textView.text = value.toString()
                    --total
                }
            }
        } catch (e: NumberFormatException) {
            context?.showToast(resources.getString(R.string.error_common), Toast.LENGTH_SHORT)
        }
    }

    private fun setResult() {
        var bundle: Bundle
        try {
            with(viewBinding) {
                bundle = bundleOf(
                    HomeFragment.KEY_DATA to listOf(
                        textPassengerNumberAdult.text.toString().toInt(),
                        textPassengerNumberChild.text.toString().toInt(),
                        textPassengerNumberInfant.text.toString().toInt(),
                    )
                )
            }
            parentFragmentManager.setFragmentResult(
                HomeFragment.KEY_ADD_PASSENGER,
                bundle
            )
            this@AddPassengerFragment.dismiss()
        } catch (e: NumberFormatException) {
            context?.showToast(resources.getString(R.string.error_common), Toast.LENGTH_SHORT)
        }
    }

    companion object {
        const val MAX_PEOPLE = 9
        const val MIN_PEOPLE = 0
        const val DEFAULT_TOTAL = 1
    }
}
