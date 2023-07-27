package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import android.graphics.Bitmap
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

interface InterfaceMain {
    interface View {
        fun setContentMarker(
            position: Int,
            pin: String,
            speed: String,
            imgUrl: String,
            load: (Bitmap) -> Unit
        )

        fun addMarkersForUsers(users: List<Users>)
    }
}