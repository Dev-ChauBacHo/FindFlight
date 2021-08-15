package com.sun.findflight.utils

import android.graphics.Color

private const val COLOR_MIN = 0
private const val COLOR_MAX = 255

object ColorUtil {
    fun getRandomColor() = Color.argb(
        COLOR_MAX,
        (COLOR_MIN..COLOR_MAX).random(),
        (COLOR_MIN..COLOR_MAX).random(),
        (COLOR_MIN..COLOR_MAX).random()
    )
}
