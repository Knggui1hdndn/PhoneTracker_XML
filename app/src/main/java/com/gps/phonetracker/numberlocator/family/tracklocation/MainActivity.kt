package com.gps.phonetracker.numberlocator.family.tracklocation

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.gps.phonetracker.numberlocator.family.tracklocation.broadcast.CurrentBattery
import com.gps.phonetracker.numberlocator.family.tracklocation.broadcast.ReceiverCharging
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityMainBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.CustomMarkerBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceMain
import com.gps.phonetracker.numberlocator.family.tracklocation.map.GoogleMap
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ConvertBitmap
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.Permission
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity(), InterfaceFireBase.View, InterfaceMain.View,
    CurrentBattery {
    private var _binding: ActivityMainBinding? = null
    private var _bindingMarker: CustomMarkerBinding? = null
    private val binding get() = _binding!!
    private var i = 0
    private val bindingMarker get() = _bindingMarker!!
    private lateinit var preGGmap: GoogleMap
    private var locationFireBase = LocationFireBase(this)
    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            if (granted.entries.all { it.value }) {

            } else {
                ShareData.Toast(this, "Vui lòng cấp quyền")
            }
        }
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiverCharging: ReceiverCharging
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _bindingMarker = binding.defaultMarker
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        preGGmap = GoogleMap(mapFragment, locationFireBase)
        intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        receiverCharging = ReceiverCharging(this)
        preGGmap.initialization() {
            locationFireBase.getListFriends()
        }
        binding.btnShare.setOnClickListener {
            if (Permission.hasPermissionLocation(this) && Permission.hasEnableGPS(this)) {
                preGGmap.registerCallBack()
                return@setOnClickListener
            }

            if (!Permission.hasPermissionLocation(this)) {
                permissionsLauncher.launch(
                    arrayOf(
                        Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_FINE_LOCATION
                    )
                )
            }
            if (!Permission.hasPermissionLocation(this)) {
                Permission.turnOnGPS(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiverCharging, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiverCharging)
    }

    override fun onChangeData(users: MutableList<Users>) {
        addMarkersForUsers(users)
    }

    override fun addMarkersForUsers(users: List<Users>) {
        lifecycleScope.launch {
            users.forEach { u ->
                delay(1000)
                locationFireBase.setOnChangeData(u.id) {
                    setContentMarker(it.pin, it.speed, it.avatar) { bitmap ->
                        val markerOption = MarkerOptions()
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(bitmap)
                            )
                            .title(it.title)
                            .position(LatLng(it.latitude, it.longitude))
                        preGGmap.setMarker(it.id, markerOption)
                    }
                }
            }
        }
    }

    override fun setContentMarker(
        pin: String, speed: String, imgUrl: String, load: (Bitmap) -> Unit
    ) {
        with(bindingMarker) {
            txtSpeed.text = speed
            txtcharging.text = pin
        }
        Picasso.get().load(imgUrl).into(binding.defaultMarker.imgAvatar, object : Callback {
            override fun onSuccess() {
                loadDelay(load)
            }

            override fun onError(e: Exception?) {
                loadDelay(load)
            }
        })
    }

    private fun loadDelay(load: (Bitmap) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.mLinear.post {
                load(ConvertBitmap.loadBitmapFromView(binding.mLinear)!!)
            }
        }, 500)
    }

    override fun onCurrentBattery(int: Int) {
        if (!ShareData.isShare) locationFireBase.putPin("$int %")
    }
}