package com.nekhaev.android.smartled.presentation.views.fairyLightView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.Exception
import kotlin.math.min


class FairyLightsView : View {

    companion object {
        private const val MIN_LIGHT_WIDTH = 200f
    }

    var fairyLightsCount
        get() = mFairyLightsCount
        private set(value) {}



    private var mFairyLightsCount = 1 // Count of fairyLights
    private var mFairyLightsWidth = MIN_LIGHT_WIDTH
    private var mFairyLightsHeight = MIN_LIGHT_WIDTH

    private var mFairyLightsList = mutableListOf<FairyLight>()

    val fairyLightsList: List<FairyLight> = mFairyLightsList

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        prepareFairyLightParams(width = w, height = h)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        val width = min(maxWidth, maxHeight)
        val height = min(maxWidth, maxHeight)


        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawFairyLights(canvas)
        }
    }

    fun setFairyLightColor(number: Int, color: Int) {
        try {
            mFairyLightsList[number].color = color
            this.invalidate()
        } catch (e: Exception) {

        }

    }

    private fun prepareFairyLightParams(width: Int, height: Int) {
        mFairyLightsCount = width / MIN_LIGHT_WIDTH.toInt()
        mFairyLightsWidth = width.toFloat() / mFairyLightsCount
        mFairyLightsHeight = height.toFloat()

        for (i in 0 until mFairyLightsCount) {
            mFairyLightsList.add(FairyLight(Color.GREEN))
        }
    }

    private fun drawFairyLights(canvas: Canvas) {
        var startX = 0f

        mFairyLightsList.map { fairyLight ->
            drawFairyLight(canvas, startX, 0f, fairyLight.color)
            startX += mFairyLightsWidth
        }
    }

    private fun drawFairyLight(canvas: Canvas, x: Float, y: Float, color: Int) {
        val lineHeight = mFairyLightsHeight
        val lineWidth = mFairyLightsWidth

        val scaleFactor = lineHeight / lineWidth

        // Matrix for scaling
        val matrix = Matrix()
        matrix.reset()
        matrix.setScale(1f, scaleFactor * 1f, x + lineWidth / 2, y)

        // Radial gradient shader
        val shader = RadialGradient(
            x + lineWidth / 2,
            0f,
            lineWidth / 2,
            color,
            Color.WHITE,
            Shader.TileMode.CLAMP
        )
        // Apply scale matrix to shader
        shader.setLocalMatrix(matrix)

        // Create pant for fairy light
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = shader

        val rect = RectF(x, y, x + lineWidth, y + lineWidth / 2)

        val path = Path()
        path.addRect(rect, Path.Direction.CW)
        path.transform(matrix)

        canvas.drawPath(path, paint)
    }
}

























