package com.gps.phonetracker.numberlocator.family.tracklocation.firebase

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceMaine
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

class LocationFireBase(private val view: InterfaceMaine.View) : InterfaceMaine.Presenter {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    override fun setOnChangeData() {
        fireBase.addSnapshotListener { qr, t ->
            if (qr != null) {
                val users = qr.toObjects(Users::class.java)
                Log.d("sapskdoaksd",users.size.toString())
                 view.onChangeData(users)
            }
        }
    }

    override fun putLocation(users: Users) {
        fireBase.add(users).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {

            }
        }
    }


}