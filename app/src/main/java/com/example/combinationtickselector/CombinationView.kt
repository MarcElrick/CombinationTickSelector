package com.example.combinationtickselector

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

const val FONT_SIZE = 20.0F

class CombinationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var numTicks: Int = 20,
    private var radius: Float = 0F,
    private var currentRotation: Float = (-PI / 2).toFloat()
) : View(context, attrs, defStyleAttr) {


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = FONT_SIZE
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private q
    val path = Path()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (height / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Radius of each circle
        val dotRad = radius * 16 / 20
        val textRad = radius * 19 / 20

        // Draw red pointer triangle
        paint.color = Color.RED
        path.moveTo(radius, radius * 7/20)
        path.lineTo(radius+10, radius* 9/20)
        path.lineTo(radius-10, radius* 9/20)
        path.lineTo(radius, radius * 7/20)
        path.close()

        canvas.drawPath(path,paint)



        // Draw numbers and ticks
        for (i in 1 until numTicks + 1) {
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
    }


}