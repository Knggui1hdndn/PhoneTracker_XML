package com.gps.phonetracker.numberlocator.family.tracklocation

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityMainBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.CustomMarkerBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceMaine
import com.gps.phonetracker.numberlocator.family.tracklocation.map.GoogleMap
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ConvertBitmap
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class MainActivity : AppCompatActivity(), InterfaceMaine.View {
    private var _binding: ActivityMainBinding? = null
    private var _bindingMarker: CustomMarkerBinding? = null
    private val binding get() = _binding!!
    private var i = 0
    private val bindingMarker get() = _bindingMarker!!
    private val presenter = LocationFireBase(this)
    private lateinit var preGGmap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _bindingMarker = binding.defaultMarker
        presenter.setOnChangeData()
        bindingMarker.root.post {
            binding.img.setImageBitmap(ConvertBitmap.loadBitmapFromView(bindingMarker.root)!!)
        }
     //   val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        preGGmap = GoogleMap(mapFragment)
//        preGGmap.initialization()
    }

    override fun onChangeData(users: MutableList<Users>) {
        i = 0
        var user = users[i]
        setContentMarker(user.pin, user.speed, user.avatar) {
            preGGmap.setMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(it)).title(user.title)
                    .position(LatLng(34.66,139.6))
            )
        }
    }

    private fun setContentMarker(
        pin: String,
        speed: String,
        imgUrl: String,
        load: (Bitmap) -> Unit
    ) {
        Picasso.get().load(imgUrl).into(bindingMarker.imgAvatar, object : Callback {
            override fun onSuccess() {
                bindingMarker.root.post {
                    load(ConvertBitmap.loadBitmapFromView(bindingMarker.root)!!)
                }
            }

            override fun onError(e: Exception?) {

            }
        })
        with(bindingMarker) {
            txtSpeed.text = speed
            txtcharging.text = pin
        }
    }
}