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
import com.manuelcarvalho.cocopic.utils.vzArray
import com.manuelcarvalho.cocopic.utils.vzColor


private const val TAG = "MapCanvas"


class MapCanvas(context: Context) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var touchX = 0.0f
    private var touchY = 0.0f

    private var canvasHeight = 0
    private var canvasWidth = 0

    private lateinit var selectPaint: Paint

    //private var vzArray = Array(64) { Array(128) { 0 } }

    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.canvasBackground, null)
    private var drawColor = ResourcesCompat.getColor(resources, R.color.canvasColor, null)


    private var paint = Paint().apply {
        color = drawColor
        style = Paint.Style.STROKE
        strokeWidth = 5f
        textSize = 20f
    }

    private var paintR = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.vzRed, null)
        style = Paint.Style.STROKE
        strokeWidth = 5f
        textSize = 20f
    }

    private var paintY = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.vzYellow, null)
        style = Paint.Style.STROKE
        strokeWidth = 5f
        textSize = 20f
    }

    private var paintB = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.vzBlue, null)
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

//        vzArray[60][60] = 1
//
//        for (x in 0..20) {
//            vzArray[30][x] = 1
//        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val xStep = canvasWidth / 128
        val yStep = canvasHeight / 64


        if (vzColor == "red") {
            selectPaint = paintR
        }
        if (vzColor == "blue") {
            selectPaint = paintB
        }
        if (vzColor == "yellow") {
            selectPaint = paintY
        }

        var Xcanvas = 0.0f
        var Ycanvas = 0.0f
        for (y1 in 0..63) {
            for (x1 in 0..127) {
                val pix = vzArray[y1][x1]
                if (pix == 1) {
                    //extraCanvas.drawPoint(Xcanvas, Ycanvas, paint)
                    selectPaint = paintR
                    selectPaint.style = Paint.Style.FILL
                    extraCanvas.drawRect(
                        Xcanvas,
                        Ycanvas,
                        (Xcanvas + xStep),
                        (Ycanvas + yStep),
                        selectPaint
                    )
                    //Log.d(TAG,"$Xcanvas   $Ycanvas ")
                }
                if (pix == 2) {
                    //extraCanvas.drawPoint(Xcanvas, Ycanvas, paint)
                    selectPaint = paintB
                    selectPaint.style = Paint.Style.FILL

                    extraCanvas.drawRect(
                        Xcanvas,
                        Ycanvas,
                        (Xcanvas + xStep),
                        (Ycanvas + yStep),
                        selectPaint
                    )
                    //Log.d(TAG,"$Xcanvas   $Ycanvas ")
                }
                if (pix == 3) {
                    //extraCanvas.drawPoint(Xcanvas, Ycanvas, paint)
                    selectPaint = paintY
                    selectPaint.style = Paint.Style.FILL

                    extraCanvas.drawRect(
                        Xcanvas,
                        Ycanvas,
                        (Xcanvas + xStep),
                        (Ycanvas + yStep),
                        selectPaint
                    )
                    //Log.d(TAG,"$Xcanvas   $Ycanvas ")
                }
                Xcanvas += xStep

            }
            Xcanvas = 0.0f
            Ycanvas += yStep
        }
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        selectPaint.style = Paint.Style.STROKE
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "On Touch")
        touchX = event.x
        touchY = event.y
        var arrayColor = 0
        val xStep = canvasWidth / 128
        val yStep = canvasHeight / 64

        var xArray = (touchX / xStep).toInt()
        var yArray = (touchY / yStep).toInt()

        if (xArray > 127) {
            xArray = 127
        }
        if (yArray > 63) {
            yArray = 63
        }

        if (vzColor == "red") {
            arrayColor = 1
        }
        if (vzColor == "blue") {
            arrayColor = 2
        }
        if (vzColor == "yellow") {
            arrayColor = 3
        }

        vzArray[yArray][xArray] = arrayColor

        Log.d(TAG, "$xArray  $yArray  ${vzArray[yArray][xArray]}")


        invalidate()

        return true
    }
}