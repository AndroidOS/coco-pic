package com.manuelcarvalho.cocopic.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

private const val TAG = "utils"
fun sendEmail(context: Context, uri: Uri) {

    val to = "tom@gmail.com"
    val subject = "Assembler Image data."
    val intent = Intent(Intent.ACTION_SEND)
    val addressees = arrayOf(to)
    intent.putExtra(Intent.EXTRA_EMAIL, addressees)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.type = "message/rfc822"
    startActivity(context, Intent.createChooser(intent, "Select Email Sending App :"), null)

}

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(
        bm, 0, 0, width, height, matrix, false
    )
    bm.recycle()
    return resizedBitmap
}