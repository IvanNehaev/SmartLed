package com.nekhaev.android.smartled.presentation.views

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

import kotlin.math.min

class ColorPickerView : View {

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    private val huePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val saturationPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        prepareColorCircle(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, radius, huePaint)
        canvas.drawCircle(centerX, centerY, radius, saturationPaint)
    }

    private fun prepareColorCircle(w: Int, h: Int) {
        val netWidth = w - paddingLeft - paddingRight
        val netHeight = h - paddingTop - paddingBottom
        radius = min(netWidth, netHeight) * 0.5f
        if (radius < 0) return
        centerX = w * 0.5f
        centerY = h * 0.5f

        val hueShader = SweepGradient(
            centerX,
            centerY,
            intArrayOf(
                Color.RED,
                Color.MAGENTA,
                Color.BLUE,
                Color.CYAN,
                Color.GREEN,
                Color.YELLOW,
                Color.RED
            ),
            null
        )

        huePaint.shader = hueShader

        val saturationShader = RadialGradient(
            centerX,
            centerY,
            radius,
            Color.WHITE,
            0x00ffffff,
            Shader.TileMode.CLAMP
        )

        saturationPaint.shader = saturationShader
    }
}