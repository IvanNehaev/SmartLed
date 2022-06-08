package com.nekhaev.android.smartled.presentation.views.fairyLightView

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

class FairyLightAnimations(
    private val view: FairyLightsView
) {

    companion object {
        private const val START_EFFECT_DURATION_MS = 2000L
        private const val LOADING_EFFECT_DURATION_MS = 1000L
        private const val ERROR_EFFECT_DURATION_MS = 1000L
        private const val LOADING_DEFAULT_INDICATOR_COLOR = Color.GREEN
    }

    private var animator: ValueAnimator? = null

    fun onStartEffect() {
        ValueAnimator.ofInt(0, 30).apply {
            duration = START_EFFECT_DURATION_MS
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                view.setFairyLightsBrightness(valueAnimator.animatedValue as Int)
            }
        }.start()

        ValueAnimator.ofArgb(Color.WHITE, Color.YELLOW).apply {
            duration = START_EFFECT_DURATION_MS
            interpolator = LinearInterpolator()
            addUpdateListener {
                view.setFairyLightsColor(it.animatedValue as Int)
            }
        }.start()
    }

    fun loadingEffect() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, view.fairyLightsCount).apply {
            duration = LOADING_EFFECT_DURATION_MS
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = REVERSE
            addUpdateListener {
                var prev = it.animatedValue as Int - 1
                var next = it.animatedValue as Int + 1

                if (prev >= 0)
                    view.setFairyLightColor(prev, Color.YELLOW)

                if (next < view.fairyLightsCount)
                    view.setFairyLightColor(next, Color.YELLOW)

                view.setFairyLightColor(it.animatedValue as Int, LOADING_DEFAULT_INDICATOR_COLOR)
            }
        }
        animator?.start()
    }

    fun errorEffect() {
        animator?.cancel()

        toSingleColorEffect(Color.RED)
    }

    fun toSingleColorEffect(@ColorInt color: Int) {
        repeat(view.fairyLightsCount) { i ->
            val lightColor = view.fairyLightsList[i].color
            ValueAnimator.ofArgb(lightColor, color).apply {
                duration = 1000L
                interpolator = LinearInterpolator()
                addUpdateListener {
                    view.setFairyLightColor(i, it.animatedValue as Int)
                }
            }.start()
        }
    }
}