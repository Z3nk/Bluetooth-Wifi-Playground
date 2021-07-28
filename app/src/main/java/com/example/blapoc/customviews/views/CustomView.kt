package com.example.blapoc.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.blapoc.R

class CustomView : View {

    // STATIC
    val margin = 10.0f

    // Variable
    private val selectorRect = RectF()
    private val selectedSelectorRect = RectF()
    private val circleCenter = PointF()
    private var rayonCircle = 0f
    private var widthSeekBar = 0f
    private var heightSeekBar = 0f

    // Colors
    private val selectorColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.black)
    }

    private val selectedSelectorColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.teal_200)
    }

    private val circleColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.purple_500)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawSelector(canvas) // The background line
        drawSelectedSelector(canvas) // The color from left of the screen to the touch point
        drawCircle(canvas) // The point to touch
    }

    private fun drawSelectedSelector(canvas: Canvas?) {
        canvas?.drawRoundRect(selectedSelectorRect, 90f, 90f, selectedSelectorColor)
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawArc(
            circleCenter.x - rayonCircle,
            circleCenter.y - rayonCircle,
            circleCenter.x + rayonCircle,
            circleCenter.y + rayonCircle,
            0f,
            360f,
            true,
            circleColor
        )
    }

    private fun drawSelector(canvas: Canvas?) {
        canvas?.drawRoundRect(selectorRect, 90f, 90f, selectorColor)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSeekBar = w.toFloat() - (margin * 2)
        heightSeekBar = h.toFloat() / 10
        rayonCircle = heightSeekBar * 3

        selectorRect.apply {
            left = margin
            right = left + widthSeekBar
            top = h.toFloat() / 2 - heightSeekBar / 2
            bottom = h.toFloat() / 2 + heightSeekBar / 2
        }

        circleCenter.apply {
            x = widthSeekBar / 2
            y = h.toFloat() / 2
        }

        selectedSelectorRect.apply {
            left = margin
            right = circleCenter.x
            top = h.toFloat() / 2 - heightSeekBar / 2
            bottom = h.toFloat() / 2 + heightSeekBar / 2
        }
    }
}