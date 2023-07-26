package com.gps.phonetracker.numberlocator.family.tracklocation

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.delay
import java.lang.Exception
import java.time.Duration
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity(), InterfaceMaine.View {
    private var _binding: ActivityMainBinding? = null
    private var _bindingMarker: CustomMarkerBinding? = null
    private val binding get() = _binding!!
    private var i = 0
    private val bindingMarker get() = _bindingMarker!!

    private lateinit var preGGmap: GoogleMap
    private var presenter = LocationFireBase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _bindingMarker = binding.defaultMarker
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        preGGmap = GoogleMap(mapFragment)
        preGGmap.initialization()
        presenter.setOnChangeData()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onChangeData(users: MutableList<Users>) {
        val listMarker = arrayListOf<MarkerOptions>()
        addMarkersForUsers(users)
    }
    private fun addMarkersForUsers(users: List<Users>) {
        val listMarker = mutableListOf<MarkerOptions>()
        val countDownLatch = CountDownLatch(users.size) // Khởi tạo CountDownLatch

        users.forEach { u ->
            setContentMarker(u.pin, u.speed, u.avatar) { bitmap ->
                val markerOption = MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .title(u.title)
                    .position(LatLng(u.latitude, u.longitude))

                synchronized(listMarker) {
                    listMarker.add(markerOption) // Thêm MarkerOption vào danh sách
                    countDownLatch.countDown() // Giảm đếm khi đã xử lý xong người dùng này
                    if (listMarker.size == users.size) {
                        try {
                            countDownLatch.await() // Đợi đến khi tất cả các công việc của setContentMarker hoàn tất
                        } catch (e: InterruptedException) {
                            // Xử lý ngoại lệ nếu cần
                        }
                        preGGmap.setMarker(listMarker) // Đặt tất cả các đánh dấu trên bản đồ
                    }
                }
            }
        }
    }

    private fun setContentMarker(
        pin: String,
        speed: String,
        imgUrl: String,
        load: (Bitmap) -> Unit
    ) {
        with(bindingMarker) {
            txtSpeed.text = speed
            txtcharging.text = pin
        }
        Picasso.get().load(imgUrl).into(bindingMarker.imgAvatar, object : Callback {
            override fun onSuccess() {

                binding.mLinear.post {
                    load(ConvertBitmap.loadBitmapFromView(binding.mLinear)!!)
                }
            }

            override fun onError(e: Exception?) {
                // Xử lý lỗi khi không tải được hình ảnh
                // (tùy theo trường hợp, có thể cần thông báo lỗi hoặc hiển thị hình ảnh mặc định)
            }
        })
    }


}