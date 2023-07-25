package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View


object ConvertBitmap {
    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(
            v.layoutParams.width,
            v.layoutParams.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }
}