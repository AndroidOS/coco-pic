package com.manuelcarvalho.cocopic.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.manuelcarvalho.cocopic.R
import com.manuelcarvalho.cocopic.utils.vzColor
import com.manuelcarvalho.cocopic.utils.vzTile


private const val TAG = "MapCanvas"


class TileCanvas(context: Context) : View(context),
    LifecycleOwner {

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

    }

    fun clearCanvas() {
        Log.d(TAG, "clearCanvas")
        extraBitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
        vzTile = Array(8) { Array(8) { 0 } }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val xStep = canvasWidth / 8
        val yStep = canvasHeight / 8


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
        for (y1 in 0..7) {
            for (x1 in 0..7) {
                val pix = vzTile[y1][x1]
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
        invalidate()
        Log.d(TAG, "On Touch")
        touchX = event.x
        touchY = event.y

        if (touchY > 650) {
            clearCanvas()
        }

        Log.d(TAG, "$touchX  $touchY")
        var arrayColor = 0
        val xStep = canvasWidth / 8
        val yStep = canvasHeight / 8

        var xArray = (touchX / xStep).toInt()
        var yArray = (touchY / yStep).toInt()

        if (xArray > 7) {
            xArray = 7
        }
        if (yArray > 7) {
            yArray = 7
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

        vzTile[yArray][xArray] = arrayColor

        Log.d(TAG, "$xArray  $yArray  ${vzTile[yArray][xArray]}")


        invalidate()

        return true
    }



    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }
}