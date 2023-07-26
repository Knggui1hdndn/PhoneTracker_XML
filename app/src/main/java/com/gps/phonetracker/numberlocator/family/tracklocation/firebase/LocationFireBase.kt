package com.gps.phonetracker.numberlocator.family.tracklocation.firebase

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import org.checkerframework.checker.units.qual.Speed

class LocationFireBase(private val view: InterfaceFireBase.View) : InterfaceFireBase.Presenter {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    private val fireBase1 = FirebaseFirestore.getInstance().collection("users")


    override fun getUsesCurrent(call: (Users) -> Unit) {
        fireBase.whereEqualTo("id", "23orf928pwVVBXhGrcwx").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val users = it.result.toObjects(Users::class.java)
                call(users[0])
            }
        }
    }

    override fun setOnChangeData(uid: String, call: (Users) -> Unit) {
        fireBase1.document(uid).addSnapshotListener { qr, t ->
            if (qr != null) {
                val user = qr.toObject(Users::class.java)
                if (user != null) {
                    Log.d("/////////////////////",user.toString())

                    call(user)
                }
            }
        }
    }

    override fun getListFriends() {
        fireBase.get().addOnSuccessListener {
            if (it != null) {
                val users = it.toObjects(Users::class.java)
                Log.d("/////////////////////",users.toString())

                view.onChangeData(users)
            }
        }
    }

    override fun putLocation(latLng: LatLng, speed: String) {
        val hash = mapOf(
            "latitude" to latLng.latitude,
            "longitude" to latLng.longitude,
            "speed" to speed

        )
        fireBase.document(ShareData.uid!!).update(hash).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {

            }
        }
    }

    override fun putPin(pin: String) {
        val hash = mapOf(
            "pin" to pin
        )
        fireBase.document(ShareData.uid!!).update(hash).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {

            }
        }
    }

}