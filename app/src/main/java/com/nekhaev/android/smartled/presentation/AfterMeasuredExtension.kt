package com.nekhaev.android.smartled.presentation

import android.view.View
import android.view.ViewTreeObserver

inline fun View.afterMeasured(crossinline f: View. () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}
