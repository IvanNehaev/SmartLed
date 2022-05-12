package com.nekhaev.android.smartled.presentation.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.*

class ColorPickerView : View {

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    private val huePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val saturationPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var onColorPicked: ((Int) -> Unit)? = null

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        val width = min(maxWidth, maxHeight)
        val height = min(maxWidth, maxHeight)

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, radius, huePaint)
        canvas.drawCircle(centerX, centerY, radius, saturationPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val action = event.actionMasked

            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    val color = getColorAtPoint(event.x, event.y)
                    onColorPicked?.let { onColorPicked ->
                        onColorPicked(color)
                    }
                }
                else -> {
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getColorAtPoint(eventX: Float, eventY: Float): Int {
        val x  = eventX.toDouble() - centerX
        val y = eventY.toDouble() - centerY
        val r: Double = sqrt(x * x + y * y)

        val hsv = floatArrayOf(0f,0f,1f)
        hsv[0] = ((atan2(y, -x) / PI * 180f) + 180).toFloat()
        hsv[1] = max(0f, min(1f, (r / radius).toFloat()))
        return Color.HSVToColor(hsv)
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