package com.gps.phonetracker.numberlocator.family.tracklocation.map

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceGoogleMap


class GoogleMap(private val smf: SupportMapFragment) : InterfaceGoogleMap.Presenter {
    private lateinit var map: GoogleMap
    override fun initialization() {
        smf.getMapAsync {
            map = it
        }
    }

    override fun setMarker(markerOptions: MarkerOptions) {
        if (this::map.isInitialized) {
            map.addMarker(markerOptions)
            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(34.66,139.6)))
        }
    }
}
