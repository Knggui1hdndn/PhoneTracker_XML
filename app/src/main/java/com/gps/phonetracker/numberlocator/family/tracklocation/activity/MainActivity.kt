package com.gps.phonetracker.numberlocator.family.tracklocation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gps.phonetracker.numberlocator.family.tracklocation.R
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
                if (!Permission.hasPermissionLocation(this)) {
                    Permission.turnOnGPS(this)
                }
            } else {
                ShareData.Toast(this, "Vui lòng cấp quyền")
            }
        }
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiverCharging: ReceiverCharging

    @SuppressLint("ResourceType")
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
                locationFireBase.onShare(true){
                    ShareData.isShare=it
                }
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
        users.forEachIndexed { index, u ->
            locationFireBase.setOnChangeData(u.id) {
                setContentMarker(index, it.pin, it.speed, it.avatar) { bitmap ->
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
    @SuppressLint("ResourceType", "InflateParams")
    override fun setContentMarker(
        position: Int,
        pin: String, speed: String, imgUrl: String, load: (Bitmap) -> Unit
    ) {
        var clonedChildView = binding.parent.getChildAt(position + 1)

        if (clonedChildView == null) {
            val inflater = LayoutInflater.from(this)
            clonedChildView = inflater.inflate(R.layout.custom_marker, null)
            binding.parent.addView(clonedChildView)
        }

        val txtSpeed = clonedChildView.findViewById<TextView>(R.id.txtSpeed)
        val txtCharging = clonedChildView.findViewById<TextView>(R.id.txtcharging)
        txtSpeed.text = speed
        txtCharging.text = pin

        Picasso.get().load(imgUrl)
            .into(clonedChildView.findViewById(R.id.imgAvatar), object : Callback {
                override fun onSuccess() {
                    loadDelay(load, clonedChildView)
                }

                override fun onError(e: Exception?) {
                    loadDelay(load, clonedChildView)
                }
            })
        loadDelay(load, clonedChildView)
    }

    private fun loadDelay(load: (Bitmap) -> Unit, clonedChildView: View) {
        Handler(Looper.getMainLooper()).postDelayed({
            clonedChildView.post {
                load(ConvertBitmap.loadBitmapFromView(clonedChildView)!!)
            }
        }, 500)
    }

    override fun onCurrentBattery(int: Int) {
         if (ShareData.isShare) locationFireBase.putPin("$int %")
    }
}