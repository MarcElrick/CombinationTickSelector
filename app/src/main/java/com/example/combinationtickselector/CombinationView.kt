package com.example.combinationtickselector

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.core.view.MotionEventCompat
import kotlin.math.*

const val FONT_SIZE = 20.0F

class CombinationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var numTicks: Int = 30,
    private var radius: Float = 0F,
    private var currentRotation: Float = (3 / 2 * PI).toFloat()
) : View(context, attrs, defStyleAttr) {

    private var hasBeenTouched = false
    private var previousX = 0F
    private var previousY = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!hasBeenTouched) {
                    previousX = event.x
                    previousY = event.y
                    hasBeenTouched = true
                }

                val angleToRotate = pointsToRotation(previousX, previousY, event.x, event.y)


                currentRotation += angleToRotate



                previousX = event.x
                previousY = event.y
                invalidate()

            }
        }
        return true
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = FONT_SIZE
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private val path = Path()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (height / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rotationToValue()

        // Radius of each circle
        val dotRad = radius * 16 / 20
        val textRad = radius * 19 / 20

        // Draw red pointer triangle
        paint.color = Color.RED
        path.moveTo(radius, radius * 7 / 20)
        path.lineTo(radius + 10, radius * 9 / 20)
        path.lineTo(radius - 10, radius * 9 / 20)
        path.lineTo(radius, radius * 7 / 20)
        path.close()

        canvas.drawPath(path, paint)


        // Draw numbers and ticks
        for (i in 0 until numTicks) {
            val t = (2 * PI * i) / numTicks
            paint.color = Color.CYAN
            val dotX = (radius + dotRad * cos(t + currentRotation)).toFloat()
            val dotY = (radius + dotRad * sin(t + currentRotation)).toFloat()
            canvas.drawCircle(dotX, dotY, 5F, paint)

            paint.color = Color.WHITE
            val textX = (radius + textRad * cos(t + currentRotation)).toFloat()
            val textY = (radius + textRad * sin(t + currentRotation)).toFloat()

            canvas.drawText(i.toString(), textX, textY + FONT_SIZE / 2, paint)
        }
        canvas.drawText(
            rotationToValue().toString(),
            radius,
            radius + paint.textSize / 2,
            paint
        )
    }

    private fun rotationToValue(): Int {
        return (round(abs((((currentRotation - (3 * PI / 2)) % (2 * PI)) * numTicks) / (2 * PI))) % numTicks).toInt()
    }

    private fun pointsToRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val m1 = (radius - y1) / (radius - x1)
        val m2 = (radius - y2) / (radius - x2)

        val tanTheta = (m2 - m1) / (1 + m2 * m1)
        return atan(tanTheta)
    }

}