package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.security.Provider

object Permission {
    fun hasPermissionLocation(context: Context): Boolean {
        val checkFind=context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val checkCoarse=context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return  checkFind|| checkCoarse
     }

    fun hasEnableGPS(context: Context): Boolean {
        val locationMng = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMng.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun turnOnGPS(context: Context, call: (Boolean) -> Unit) {
        val request = LocationRequest
            .Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateDelayMillis(1000)
            .build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(context as Activity, 12345)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }.addOnSuccessListener {
            call(true)
        }
    }
}