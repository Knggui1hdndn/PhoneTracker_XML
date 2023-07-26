package com.gps.phonetracker.numberlocator.family.tracklocation.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class Location(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val currentLocationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setDurationMillis(1000)
        .setMaxUpdateAgeMillis(1000)
        .build()

    @SuppressLint("MissingPermission")
    fun getFromLocation(call: (String) -> Unit) {
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