package com.nekhaev.android.smartled.presentation.views.fairyLightView

import android.graphics.Paint
import androidx.annotation.ColorInt

class FairyLight(
    @ColorInt
    var color: Int,
    brightness: Int
) {
    var brightness: Int = brightness
        set(value) {
            field = if (value in 1..100) value else if (value > 100) 100 else 1
        }
}