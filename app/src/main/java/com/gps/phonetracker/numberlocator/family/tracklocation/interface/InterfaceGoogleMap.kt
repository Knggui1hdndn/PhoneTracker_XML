package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import com.google.android.gms.maps.model.MarkerOptions

interface InterfaceGoogleMap {
    interface View{

    }
    interface Presenter{
        fun initialization()
        fun setMarker(markerOptions:MarkerOptions)
    }
}