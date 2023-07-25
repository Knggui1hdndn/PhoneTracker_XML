package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.WindowManager


object ConvertBitmap {
    fun loadBitmapFromView(v: View): Bitmap? {
        val dm = v.context.resources.displayMetrics
        v.measure(
            View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY)
        )
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val returnedBitmap = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val c = Canvas(returnedBitmap)
        v.draw(c)
        return returnedBitmap
    }
}