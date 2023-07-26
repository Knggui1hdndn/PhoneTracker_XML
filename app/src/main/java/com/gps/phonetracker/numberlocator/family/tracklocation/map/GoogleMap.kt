package com.gps.phonetracker.numberlocator.family.tracklocation.map

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceGoogleMap
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import java.util.concurrent.ConcurrentHashMap


class GoogleMap(private val smf: SupportMapFragment, private val fileBase: LocationFireBase) :
    InterfaceGoogleMap.Presenter {
    @SuppressLint("UseRequireInsteadOfGet")
    private val context = smf.context!!
    private lateinit var map: GoogleMap
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setGranularity(Granularity.GRANULARITY_FINE)
        .setMinUpdateDistanceMeters(0f)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(100)
        .setMaxUpdateDelayMillis(100)
        .build()
    private lateinit var currentUser: Users
    private val currentLocationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            if (lastLocation != null) {
                var speed = 0.0
                if (lastLocation.hasSpeed()) {
                    speed = lastLocation.speed * 3.6
                }
                fileBase.putLocation(
                    latLng = LatLng(
                        lastLocation.latitude,
                        lastLocation.longitude
                    ), "${speed.toInt()} km/h"
                )
            }
        }
    }


    override fun initialization(call: () -> Unit) {
        smf.getMapAsync {
            map = it
//            it.setMaxZoomPreference(10f)
//            it.setMinZoomPreference(10f)
            call()
        }
    }

    @SuppressLint("MissingPermission")
    override fun registerCallBack() {
        fileBase.getUsesCurrent {
            currentUser = it
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun removeCallBack() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val marker = HashMap<String, MarkerOptions>()

    override fun setMarker(uid: String, markerOptions: MarkerOptions) {
        if (this::map.isInitialized) {

            marker[uid] = markerOptions
            map.addMarker(markerOptions)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getFromLocation(call: (String) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()
        val cancellationToken = cancellationTokenSource.token
        fusedLocationClient.getCurrentLocation(currentLocationRequest, cancellationToken)
            .addOnSuccessListener {
                val geo = Geocoder(context)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val location = geo.getFromLocation(it.latitude, it.longitude, 1) {
                        if (it.isNotEmpty()) {
                            if (it[0] != null) {
                                call(it[0].getAddressLine(0))
                            }
                        }
                    }
                } else {
                    val location = geo.getFromLocation(it.latitude, it.longitude, 1).let {
                        if (it != null) {
                            if (it.isNotEmpty()) {
                                if (it[0] != null) {
                                    call(it[0].getAddressLine(0))
                                }
                            }
                        }
                    }
                }
            }
    }
}
