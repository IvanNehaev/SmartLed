package com.nekhaev.android.smartled.presentation.views

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.icu.text.MessagePattern
import android.util.AttributeSet
import android.view.View
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.core.graphics.set
import java.lang.Exception
import java.lang.Math.pow
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ColorPickerView : View {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val size = 800
    private val mRadius = size / 2
    private val borderWidth = 4.0f
    private val colorPallet = preparePallet()
    private val colorCount: Int = colorPallet.size
    private val colorIndexCoefficient = colorCount.toFloat() / size

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //drawPicker(canvas)
        //drawCirclePicker(canvas)
        drawCirclePickerExperimental(canvas)
    }

    private fun drawPicker(canvas: Canvas) {

        var index = 0
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (i in 0 until bitmap.width) {
            index = (i * colorIndexCoefficient).roundToInt()
            for (j in 0 until bitmap.height)
                bitmap[i, j] = colorPallet[index]
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private fun drawCirclePickerExperimental(canvas: Canvas) {
        var index = 0
        var colorIndex = 0
        val coeff = colorCount.toFloat() / (size * 2)


        val circleSize = size
        val bitmap = Bitmap.createBitmap(circleSize, circleSize, Bitmap.Config.ARGB_8888)
        val radius = (circleSize / 2.0).roundToInt()
        val fadeCoeff = 255.0 / radius
        var fadeIndex = 0
        var fadeCounter = 0

        var a = radius
        var b = radius

        for (r in radius downTo 0) {
            fadeIndex = (++fadeCounter * fadeCoeff).roundToInt()
            for (x in 0..radius) {
                index = (++colorIndex * coeff).roundToInt()
                for (y in (radius) downTo 0) {
                    val c = sqrt((x.toDouble() - a).pow(2.0) + (y.toDouble() - b).pow(2.0))
                    if (r == c.roundToInt()) {
                        try {
                            bitmap[x, y] = fadeColor(colorPallet[index], fadeIndex)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
            for (x in radius until bitmap.width) {
                index = (++colorIndex * coeff).roundToInt()
                for (y in 0..radius) {
                    val c = sqrt((x.toDouble() - a).pow(2.0) + (y.toDouble() - b).pow(2.0))
                    if (r == c.roundToInt()) {
                        try {
                            bitmap[x, y] = fadeColor(colorPallet[index], fadeIndex)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
            for (x in (bitmap.width - 1) downTo radius) {
                index = (++colorIndex * coeff).roundToInt()
                for (y in radius until bitmap.width) {
                    val c = sqrt((x.toDouble() - a).pow(2.0) + (y.toDouble() - b).pow(2.0))
                    if (r == c.roundToInt()) {
                        try {
                            bitmap[x, y] = fadeColor(colorPallet[index], fadeIndex)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
            for (x in (radius-1) downTo 0) {
                index = (++colorIndex * coeff).roundToInt()
                for (y in bitmap.width downTo radius) {
                    val c = sqrt((x.toDouble() - a).pow(2.0) + (y.toDouble() - b).pow(2.0))
                    if (r == c.roundToInt()) {
                        try {
                            bitmap[x, y] = fadeColor(colorPallet[index], fadeIndex)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
            colorIndex = 0
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private fun fadeColor(color: Int, fadeIndex: Int): Int {
        var c = color shl 8
        c = c ushr 8
        c = c ushr 8
        c = c ushr 8

        var r = (color shl 8) ushr 24
        var g = (color shl 16) ushr 24
        var b = (color shl 24) ushr 24

        r = increaseColorChanel(r, fadeIndex)
        g = increaseColorChanel(g, fadeIndex)
        b = increaseColorChanel(b, fadeIndex)

        val newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
        return newColor
    }

    private fun increaseColorChanel(channelValue: Int, step: Int): Int {
        return if (channelValue + step > 255){
            255
        } else {
            channelValue + step
        }
    }

    private fun drawCirclePicker(canvas: Canvas) {
        var index = 0
        val bitmap = Bitmap.createBitmap(size + 50, size + 55, Bitmap.Config.ARGB_8888)
        val radius = (size / 2.0).roundToInt()
        val a = radius
        val b = radius + 50
        for (i in 0 until bitmap.width) {
            index = (i * colorIndexCoefficient).roundToInt()
            for (j in 0 until bitmap.height) {

                val x = i.toDouble()
                val y = j.toDouble()
                val c = sqrt((x - a).pow(2.0) + (y - b).pow(2.0))

                if (radius >= c.roundToInt()) {
                    try {
                        bitmap[i, j] = colorPallet[index]
                    } catch (e: Exception) {

                    }
                }
            }

        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private fun prepareFullPallet(): ArrayList<Int> {
        val colors = arrayListOf<Int>()
        var newColor: Int

        var r: Int = 255
        var g: Int = 0
        var b: Int = 0
        // Increase green
        for (i in 0..255) {
            newColor = (255 shl 24) + (r shl 16) + (i shl 8)
            colors.add(newColor)
            for (j in 0..255) {
                if (g + 1 < 256) {
                    g++
                }
                if (b + 1 < 256) {
                    b++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            g = i
            b = 0
        }
        // Decrease red
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (i shl 16) + (255 shl 8)
            colors.add(newColor)
            for (j in 0..255) {
                if (r + 1 < 256) {
                    r++
                }
                if (b + 1 < 256) {
                    b++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            r = i
            b = 0
        }
        r = 0
        g = 255
        b = 0
        // Increase blue
        for (i in 0..255) {
            newColor = (255 shl 24) + (255 shl 8) + i
            colors.add(newColor)
            for (j in 0..255) {
                if (r + 1 < 256) {
                    r++
                }
                if (b + 1 < 256) {
                    b++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            r = 0
            b = i
        }
        r = 0
        g = 255
        b = 255
        // Decrease green
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (i shl 8) + 255
            colors.add(newColor)
            for (j in 0..255) {
                if (g + 1 < 256) {
                    g++
                }
                if (r + 1 < 256) {
                    r++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            r = 0
            g = i
        }
        r = 0
        g = 0
        b = 255
        // Increase red
        for (i in 0..255) {
            newColor = (255 shl 24) + (i shl 16) + 255
            colors.add(newColor)
            for (j in 0..255) {
                if (r + 1 < 256) {
                    r++
                }
                if (g + 1 < 256) {
                    g++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            r = i
            g = 0
        }
        r = 255
        g = 0
        b = 255
        // Decrease blue
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (255 shl 16) + i
            colors.add(newColor)
            for (j in 0..255) {
                if (g + 1 < 256) {
                    g++
                }
                if (b + 1 < 256) {
                    b++
                }
                newColor = (255 shl 24) + (r shl 16) + (g shl 8) + b
                colors.add(newColor)
            }
            b = i
            g = 0
        }
        return colors
    }

    private fun preparePallet(): ArrayList<Int> {
        val colors = arrayListOf<Int>()
        var newColor: Int
        // Increase green
        for (i in 0..255) {
            newColor = (255 shl 24) + (255 shl 16) + (i shl 8)
            colors.add(newColor)
        }
        // Decrease red
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (i shl 16) + (255 shl 8)
            colors.add(newColor)
        }
        // Increase blue
        for (i in 0..255) {
            newColor = (255 shl 24) + (255 shl 8) + i
            colors.add(newColor)
        }
        // Decrease green
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (i shl 8) + 255
            colors.add(newColor)
        }
        // Increase red
        for (i in 0..255) {
            newColor = (255 shl 24) + (i shl 16) + 255
            colors.add(newColor)
        }
        // Decrease blue
        for (i in 255 downTo 0) {
            newColor = (255 shl 24) + (255 shl 16) + i
            colors.add(newColor)
        }
        return colors
    }

    //    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
//    }
    private class ARGB(var a: Int = 255, var r: Int = 0, var g: Int = 0, var b: Int = 0)
}