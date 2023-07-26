package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View


object ConvertBitmap {

    fun loadBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(180, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}