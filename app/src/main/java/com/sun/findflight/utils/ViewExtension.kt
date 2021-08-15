package com.sun.findflight.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.RadioButton

private const val ANIM_FROM = 0.0F
private const val ANIM_TO = 1.0F
private const val ANIM_PIVOT = 0.5F
private const val ANIM_DURATION = 300L

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.setFadeAnimation() {
    val anim = ScaleAnimation(
        ANIM_FROM,
        ANIM_TO,
        ANIM_FROM,
        ANIM_TO,
        Animation.RELATIVE_TO_SELF,
        ANIM_PIVOT,
        Animation.RELATIVE_TO_SELF,
        ANIM_PIVOT
    )
    anim.duration = ANIM_DURATION
    this.startAnimation(anim)
}

fun RadioButton.setChecked() = run { this.isChecked = true }
