package com.manuelcarvalho.cocopic.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.manuelcarvalho.cocopic.R


private const val TAG = "MapCanvas"


class MapCanvas(context: Context) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var touchX = 0.0f
    private var touchY = 0.0f

    private var canvasHeight = 0
    private var canvasWidth = 0

    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.canvasBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.canvasColor, null)


    private val paint = Paint().apply {
        color = drawColor
        style = Paint.Style.STROKE
        strokeWidth = 5f
        textSize = 20f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        canvasWidth = w
        canvasHeight = h / 2


        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        touchX = event.x
        touchY = event.y

//        for (x in -canvasWidth..canvasWidth) {
//            Log.d(TAG, " $x")
//            val x1 = x * touchX / 10
        extraCanvas.drawPoint(touchX, touchY, paint)
        // }

        invalidate()

        return true
    }
}