package com.sun.findflight.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sun.findflight.R

fun ImageView.loadImage(image: String) {
    Glide.with(context)
        .load(image)
        .error(R.drawable.ic_flag_holder)
        .placeholder(R.drawable.ic_flag_holder)
        .into(this)
}
