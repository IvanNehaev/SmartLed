package com.nekhaev.android.smartled.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FairyLightsView : View {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintBlack = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mShader = createShader()

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mPaint.shader = mShader

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintBlack.color = Color.BLACK
        canvas?.drawRect(99f, 99f, 401f, 301f, mPaintBlack)

        canvas?.drawRect(100f, 100f, 400f, 300f, mPaint)
    }

    private fun createShader(): Shader {
        return LinearGradient(100f,0f,400f,0f,
            Color.RED,
            Color.WHITE,
            Shader.TileMode.CLAMP
        )

//        return LinearGradient(100f,0f, 400f, 0f,
//        intArrayOf(Color.RED, Color.WHITE),
//        floatArrayOf(0f, 1f),
//        Shader.TileMode.CLAMP)

    }
}