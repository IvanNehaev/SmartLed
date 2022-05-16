package com.nekhaev.android.smartled.presentation.views.fairyLightView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.transform
import kotlin.math.min


class FairyLightsView : View {

    companion object {
        private const val MIN_LIGHT_WIDTH = 200
    }


    private var mFairyLightsCount = 1 // Count of fairyLights
    private var mFairyLightsWidth = 100f
    private var mFairyLightsHeight = 100f

    private var mFairyLightsList = mutableListOf<FairyLight>()

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
//            drawTestLines(canvas, 0f, 0f, Color.RED)
//            drawTestLines(canvas, 300f, 0f, Color.GREEN)
//            drawTestLines(canvas, 600f, 0f, Color.BLUE)
        }
    }

    private fun createShader(height: Float, startColor: Int, endColor: Int): Shader {
        return LinearGradient(
            0f, 0f, 0f, height,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
    }

    private fun createHorizontalGradShader(
        width: Float,
        startColor: Int,
        endColor: Int
    ): Shader {
        return LinearGradient(
            0f, 0f, width, 0f,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
    }

    private fun prepareFairyLightParams(width: Int, height: Int) {
        mFairyLightsCount = width / MIN_LIGHT_WIDTH
        mFairyLightsWidth = width.toFloat() / mFairyLightsCount
        mFairyLightsHeight = height.toFloat()


        for (i in 0 until mFairyLightsCount) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = createShader(
                height = mFairyLightsHeight,
                startColor = Color.RED,
                endColor = Color.WHITE
            )

            mFairyLightsList.add(FairyLight(paint))
        }

    }

    private fun drawFairyLights(canvas: Canvas) {
        var startX = 0f
        var endX = mFairyLightsWidth

        mFairyLightsList.map { fairyLight ->
//            canvas.drawRect(startX, 0f, endX, mFairyLightsHeight, fairyLight.paint)
//            startX += mFairyLightsWidth
//            endX += mFairyLightsWidth
            drawTestLines(canvas, startX, 0f, Color.MAGENTA)
            startX += mFairyLightsWidth
        }
    }

    private fun drawTestLines(canvas: Canvas, x: Float, y: Float, color: Int) {
        val lineHeight = mFairyLightsHeight
        val lineWidth = mFairyLightsWidth

        val scaleFactor = lineHeight / lineWidth

        // Matrix for scaling
        val matrix = Matrix()
        matrix.reset()
        matrix.setScale(1f, scaleFactor*1.5f, x + lineWidth / 2, y)

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

























