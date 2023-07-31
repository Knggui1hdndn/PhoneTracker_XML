package com.gps.phonetracker.numberlocator.family.tracklocation.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import org.mindrot.jbcrypt.BCrypt

class LocationFireBase( ) :
    InterfaceFireBase.Presenter.LiveLocation {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    private val fireBase1 = FirebaseFirestore.getInstance().collection("users")
    override fun onShare(boolean: Boolean, call: (Boolean) -> Unit) {
        fireBase.document(ShareData.uid).update(mapOf("share" to boolean))
            .addOnSuccessListener { call(boolean) }
    }

    override fun setOnChangeData(uid: String, call: (Users) -> Unit) {
        fireBase1.document(uid).addSnapshotListener { qr, t ->
            if (qr != null) {
                val user = qr.toObject(Users::class.java)
                if (user != null) {
                    call(user)
                }
            }
        }
    }

    override fun putLocation(latLng: LatLng, speed: String) {
        val hash = mapOf(
            "latitude" to latLng.latitude,
            "longitude" to latLng.longitude,
            "speed" to speed
        )
        fireBase.document(ShareData.uid).update(hash)
    }

    override fun putPin(pin: String) {
        val hash = mapOf(
            "pin" to pin
        )
        fireBase.document(ShareData.uid).update(hash)
    }
}