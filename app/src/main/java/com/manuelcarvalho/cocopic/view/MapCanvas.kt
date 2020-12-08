package com.manuelcarvalho.cocopic.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
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

    private var vzArray = Array(64) { Array(128) { 0 } }

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


        extraBitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        vzArray[60][60] = 1

        for (x in 0..20) {
            vzArray[30][x] = 1
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "On Touch")
        touchX = event.x
        touchY = event.y

        val xStep = canvasWidth / 128
        val yStep = canvasHeight / 64

        Log.d(TAG, "$xStep  $yStep ")

        var Xcanvas = 0.0f
        var Ycanvas = 0.0f
        for (y1 in 0..63) {
            for (x1 in 0..127) {
                val pix = vzArray[y1][x1]
                if (pix > 0) {
                    //extraCanvas.drawPoint(Xcanvas, Ycanvas, paint)
                    extraCanvas.drawRect(
                        Xcanvas,
                        Ycanvas,
                        (Xcanvas + xStep),
                        (Ycanvas + yStep),
                        paint
                    )
                    //Log.d(TAG,"$Xcanvas   $Ycanvas ")
                }
                Xcanvas += xStep

            }
            Xcanvas = 0.0f
            Ycanvas += yStep
        }
//        for (x in -canvasWidth..canvasWidth) {
//            Log.d(TAG, " $x")
//            val x1 = x * touchX / 10
        //extraCanvas.drawPoint(touchX, touchY, paint)
        // }

        invalidate()

        return true
    }
}