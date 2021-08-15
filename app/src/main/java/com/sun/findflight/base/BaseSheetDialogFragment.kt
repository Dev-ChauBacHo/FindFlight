package com.sun.findflight.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseSheetDialogFragment<V : ViewBinding> : BottomSheetDialogFragment() {

    private var _viewBinding: ViewBinding? = null
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> V
    protected val viewBinding: V
        get() = _viewBinding as V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = bindingInflater.invoke(inflater, container, false)
        return _viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    protected abstract fun initEvents()
    protected abstract fun initData()
}
