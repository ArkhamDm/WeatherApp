package dev.arkhamd.wheatherapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class Circle(context: Context): View(context) {
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(50f, 50f, 25f, paint)
    }

}