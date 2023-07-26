package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import com.google.android.gms.maps.model.MarkerOptions

interface InterfaceGoogleMap {
    interface View {

    }

    interface Presenter {
        fun getFromLocation(call: (String) -> Unit)
        fun setMarker(uid:String,markerOptions: MarkerOptions)
        fun initialization(call: () -> Unit)
        fun registerCallBack()
        fun removeCallBack()
    }
}