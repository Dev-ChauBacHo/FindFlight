package com.sun.findflight.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.datepicker.MaterialDatePicker
import com.sun.findflight.ui.searchPlace.SearchPlaceFragment

fun FragmentManager.addFragment(layout: Int, fragment: Fragment, tag: String? = null ) {
    beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(layout, fragment, tag)
        .addToBackStack(null)
        .commit()
}

fun FragmentManager.removeFragment(layout: Int, fragment: Fragment) {
    popBackStack()
    beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        .remove(fragment)
        .commit()
}

fun FragmentManager.replaceFragment(layout: Int, fragment: Fragment) {
    beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .replace(layout, fragment)
        .addToBackStack(null)
        .commit()
}

fun FragmentManager.clearBackStack() = apply {
    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentManager.detach(fragment: Fragment) {
    beginTransaction()
        .detach(fragment)
        .commit()
}

fun FragmentManager.attach(fragment: Fragment) {
    beginTransaction()
        .attach(fragment)
        .commit()
}

fun FragmentManager.chooseDate(textView: TextView, title: String) {
    val datePicker = MaterialDatePicker
        .Builder
        .datePicker()
        .setTitleText(title)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
    datePicker.addOnPositiveButtonClickListener {
        val result = datePicker.selection
        result?.let { textView.text = TimeUtils.convertToDate(it) }
    }
    datePicker.show(this, null)
}

fun Fragment.closeKeyboard() = activity?.currentFocus?.let {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(it.windowToken, SearchPlaceFragment.FLAG_OPERATING)
}
