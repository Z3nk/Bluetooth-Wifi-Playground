package com.example.blapoc.customviews.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.blapoc.R
import com.leroymerlin.enki.main.control.customs.computeDistanceSegment
import com.leroymerlin.enki.main.control.customs.computePositionOnCircle
import com.leroymerlin.enki.main.control.customs.convertValueToDegree

class SeekBarBrightnessView : View {

    private var widthView: Float = 0.0f
    private var heightView: Float = 0.0f

    private val rectSeekBarBackgroung = RectF()
    private val rectSeekBarSelected = RectF()
    private val pointSelector = PointF(0f, 0f)

    private var onSeekBarListener: OnSeekBarListener? = null

    private var deltaPosition = 0f
    private var startTrack = 0f
    private var endTrack = 0f
    private val minValue = 0
    private val maxValue = 100

    private val sunStrokeWidth = 4f

    private val radiusBlur = 10f

    private var isDisable = false

    private val seekBarBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.black)
    }

    private val seekBarSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val selectorPaintBis = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.white)
    }

    private val selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.white)
    }

    private val selectorBlurPaint = Paint().apply {
        set(selectorPaint)
        maskFilter = BlurMaskFilter(radiusBlur, BlurMaskFilter.Blur.NORMAL)
        color = resources.getColor(R.color.white)
    }

    private val selectorSunPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = sunStrokeWidth
        color = resources.getColor(R.color.material_on_primary_disabled)
        strokeCap = Paint.Cap.ROUND
    }

    private var progress = 50

    private var rayonSelector = 0f
    private val deltaSun = 360 / 8

    private val dataSun = DataSun()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        widthView = w.toFloat()
        heightView = h.toFloat()

        val heightSeekBar = (heightView / 30) * 4
        val diameterSelector = (heightSeekBar * 5)
        val diameterSun = (diameterSelector / 6)
        rayonSelector = diameterSelector / 2
        dataSun.rayonSun = diameterSun / 2
        val margin = radiusBlur
        val widthSeekBar = widthView - (margin * 2)

        rectSeekBarBackgroung.apply {
            left = margin
            top = (heightView - heightSeekBar) / 2
            right = left + widthSeekBar
            bottom = top + heightSeekBar
        }

        val marginTrack = rayonSelector
        startTrack = rectSeekBarBackgroung.left + marginTrack
        endTrack = rectSeekBarBackgroung.right - marginTrack

        deltaPosition = (endTrack - startTrack) / (maxValue - minValue)

        rectSeekBarSelected.apply {
            left = rectSeekBarBackgroung.left
            right = left + rayonSelector + (deltaPosition * progress)
            top = rectSeekBarBackgroung.top
            bottom = rectSeekBarBackgroung.bottom
        }

        pointSelector.apply {
            x = startTrack + (deltaPosition * progress)
            y = heightView / 2
        }

        seekBarSelectedPaint.shader = LinearGradient(
                0f,
                0f,
                rectSeekBarSelected.right,
                0f,
                resources.getColor(R.color.white00),
                resources.getColor(R.color.white100),
                Shader.TileMode.MIRROR
        )

        computeSunLine()
    }

    private fun computeSunLine() {
        dataSun.listPoint.clear()
        val yStart = pointSelector.y - dataSun.rayonSun - (sunStrokeWidth * 3)
        val yEnd = yStart + sunStrokeWidth
        val dataLine = DataLine(PointF(pointSelector.x, yStart), PointF(pointSelector.x, yEnd))
        dataSun.listPoint.add(dataLine)
        for (i in 1..7) {
            val angle = convertValueToDegree(deltaSun * i)
            dataSun.listPoint.add(
                    DataLine(
                            computePositionOnCircle(angle, dataLine.pointStart, pointSelector),
                            computePositionOnCircle(angle, dataLine.pointEnd, pointSelector)
                    )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawSeekBar(canvas)
        drawSeekBarSelected(canvas)
        drawSelector(canvas)
        drawSun(canvas)
    }

    private fun drawSeekBar(canvas: Canvas) {
        canvas.drawRoundRect(
                rectSeekBarBackgroung,
                90f,
                90f,
                seekBarBackgroundPaint
        )
    }

    private fun drawSeekBarSelected(canvas: Canvas) {
        canvas.drawRoundRect(
                rectSeekBarSelected,
                90f,
                90f,
                seekBarSelectedPaint
        )
    }

    private fun drawSelector(canvas: Canvas) {
        canvas.drawArc(
                pointSelector.x - rayonSelector,
                pointSelector.y - rayonSelector,
                pointSelector.x + rayonSelector,
                pointSelector.y + rayonSelector,
                0f,
                360f,
                true,
                selectorPaintBis
        )
        canvas.drawArc(
                pointSelector.x - rayonSelector,
                pointSelector.y - rayonSelector,
                pointSelector.x + rayonSelector,
                pointSelector.y + rayonSelector,
                0f,
                360f,
                true,
                selectorPaint
        )
        if (!isDisable) {
            canvas.drawArc(
                    pointSelector.x - rayonSelector,
                    pointSelector.y - rayonSelector,
                    pointSelector.x + rayonSelector,
                    pointSelector.y + rayonSelector,
                    0f,
                    360f,
                    true,
                    selectorBlurPaint
            )
        }
    }

    private fun drawSun(canvas: Canvas) {
        canvas.drawArc(
                pointSelector.x - dataSun.rayonSun,
                pointSelector.y - dataSun.rayonSun,
                pointSelector.x + dataSun.rayonSun,
                pointSelector.y + dataSun.rayonSun,
                0f,
                360f,
                true,
                selectorSunPaint
        )
        dataSun.listPoint.forEach {
            canvas.drawLine(
                    it.pointStart.x,
                    it.pointStart.y,
                    it.pointEnd.x,
                    it.pointEnd.y,
                    selectorSunPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isDisable) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (computeDistanceSegment(
                                    pointSelector,
                                    PointF(event.x, event.y)
                            ) <= rayonSelector
                    ) {
                        progress = ((event.y - rectSeekBarBackgroung.top) / deltaPosition).toInt()
                        invalidate()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val value = ((event.x - startTrack) / deltaPosition).toInt()

                    if (value < minValue) {
                        progress = minValue
                    } else if (value > maxValue) {
                        progress = maxValue
                    } else {
                        progress = value
                    }

                    val newPosX = startTrack + deltaPosition * progress
                    val deltaX = pointSelector.x - newPosX
                    pointSelector.apply {
                        x = newPosX
                    }

                    dataSun.listPoint.forEach {
                        it.pointStart.apply {
                            x -= deltaX
                        }
                        it.pointEnd.apply {
                            x -= deltaX
                        }
                    }

                    rectSeekBarSelected.apply {
                        right = left + rayonSelector + (deltaPosition * progress)
                    }
                    seekBarSelectedPaint.shader = LinearGradient(
                            0f,
                            0f,
                            rectSeekBarSelected.right,
                            0f,
                            resources.getColor(R.color.white00),
                            resources.getColor(R.color.white100),
                            Shader.TileMode.MIRROR
                    )
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    onSeekBarListener?.onChangedValueListener(progress, maxValue)
                    invalidate()
                }
            }
        }
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (parent != null && event.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }

    fun setOnSeekBarListener(l: OnSeekBarListener) {
        onSeekBarListener = l
    }

    fun setProgress(value: Int) {
        progress = value

        rectSeekBarSelected.apply {
            right = left + rayonSelector + (deltaPosition * progress)
        }

        pointSelector.apply {
            x = startTrack + deltaPosition * progress
        }

        if (!isDisable) {
            seekBarSelectedPaint.shader = LinearGradient(
                    0f,
                    0f,
                    rectSeekBarSelected.right,
                    0f,
                    resources.getColor(R.color.white00),
                    resources.getColor(R.color.white100),
                    Shader.TileMode.MIRROR
            )
        }

        computeSunLine()

        invalidate()
    }

    fun disableSeekBar() {
        isDisable = true
        selectorPaint.color = resources.getColor(R.color.material_on_primary_disabled)
        selectorSunPaint.color = Color.BLACK
        seekBarSelectedPaint.shader = LinearGradient(
                0f,
                0f,
                rectSeekBarSelected.right,
                0f,
                resources.getColor(R.color.white00),
                resources.getColor(R.color.material_on_primary_disabled),
                Shader.TileMode.MIRROR
        )
        invalidate()
    }


    fun enableSeekBar() {
        isDisable = false
        selectorPaint.color = resources.getColor(R.color.white)
        selectorSunPaint.color = resources.getColor(R.color.material_on_primary_disabled)
        seekBarSelectedPaint.shader = LinearGradient(
                0f,
                0f,
                rectSeekBarSelected.right,
                0f,
                resources.getColor(R.color.white00),
                resources.getColor(R.color.white100),
                Shader.TileMode.MIRROR
        )
        invalidate()
    }

    interface OnSeekBarListener {
        fun onChangedValueListener(value: Int, max: Int)
    }

    data class DataSun(
            val angle: Int = 360 / 8,
            var rayonSun: Float = 0f,
            val listPoint: MutableList<DataLine> = mutableListOf()
    )

    data class DataLine(var pointStart: PointF, var pointEnd: PointF)
}