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

class LocationFireBase(private val view: InterfaceFireBase.View?) : InterfaceFireBase.Presenter {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    private val fireBase1 = FirebaseFirestore.getInstance().collection("users")
    private val storageRef = FirebaseStorage.getInstance().reference.child("image")


    override fun getUsesCurrent(call: (Users?) -> Unit) {
        if (ShareData.uid != "") fireBase.whereEqualTo("id", ShareData.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = it.result.toObjects(Users::class.java)
                    call(users[0])
                } else {
                    call(null)
                }
            }
    }

    override fun onShare(boolean: Boolean, call: (Boolean) -> Unit) {
        fireBase.document(ShareData.uid).update(mapOf("share" to boolean))
            .addOnSuccessListener { call(boolean) }
    }

    override fun addFriends(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            updateFriendByField(uid, "friends", it.friends, call)
        }
    }

    override fun addSentFriendRequest(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            updateFriendByField(uid, "sentFriendRequest", it.sentFriendRequest, call)
        }
    }

    override fun addReceivedFriendRequest(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            updateFriendByField(uid, "receivedFriendRequest", it.receivedFriendRequest, call)
        }
    }

    override fun removeFriends(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            val list = it.friends.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "sentFriendRequest", list.toList(), call)
        }
    }

    private fun updateFriendByField(
        uid: String,
        field: String,
        list: List<String>,
        call: (Boolean, String?) -> Unit
    ) {
        fireBase.document(uid).update(mapOf(field to list)).addOnSuccessListener {
            call(true, null)
        }.addOnFailureListener {
            call(false, "Xảy ra lỗi")
        }
    }

    override fun removeSentFriendRequest(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            val list = it.sentFriendRequest.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "sentFriendRequest", list.toList(), call)
        }
    }

    override fun removeReceivedFriendRequest(uid: String, call: (Boolean, String?) -> Unit) {
        getUsesCurrent {
            if (it == null) {
                call(false, "Xảy ra lỗi")
                return@getUsesCurrent
            }
            val list = it.receivedFriendRequest.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "receivedFriendRequest", list.toList(), call)
        }
    }

    override fun checkPhoneNumberNotExist(phoneNumber: String, call: (Boolean) -> Unit) {
        fireBase.whereEqualTo("phoneNumber", phoneNumber).get().addOnCompleteListener {
            if (it.isSuccessful) {
                call(it.result.isEmpty)
            }
        }
    }

    override fun login(phoneNumber: String, pass: String, call: (Users?, String?) -> Unit) {
        fireBase.whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (!it.result.isEmpty) {
                        val users = it.result.toObjects(Users::class.java)
                        if (BCrypt.checkpw(pass, users[0].pass)) {
                            ShareData.currentUser = users[0]
                            call(users[0], null)
                        }
                    }
                }
            }
    }

    override fun register(users: Users, call: (Boolean, String?) -> Unit) {
        checkPhoneNumberNotExist(users.phoneNumber) { phoneNumberNotExist ->
            if (phoneNumberNotExist) {
                fireBase.add(users).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val id = task.result.id
                        updateIdUserWithIdDocument(id) { updateSuccess ->
                            if (!updateSuccess) {
                                fireBase.document(id).delete()
                            }
                            call(updateSuccess, null)
                        }
                    }
                }
            } else {
                call(false, "Số điện thoại đã được đăng kí")
            }
        }
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


    override fun getListFriends() {
        getUsesCurrent {
            if (it != null) {
                fireBase1.whereIn("id", it.friends).get().addOnSuccessListener {
                    if (it != null) {
                        val users = it.toObjects(Users::class.java)
                        view?.onChangeData(users)
                    }
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

    override fun putImage(uri: Uri, call: (String?) -> Unit) {
        storageRef.child("/${ShareData.uid}.png").putFile(uri).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) call(it.result.toString()) else call(null)
                }
            } else {
                call(null)
            }
        }
    }

    override fun updateIdUserWithIdDocument(id: String, call: (Boolean) -> Unit) {
        fireBase.document(id).update(mapOf("id" to id)).addOnCompleteListener {
            call(it.isSuccessful)
        }
    }

    override fun updateImage(url: String, call: (Boolean) -> Unit) {
        ShareData.uid.let {
            fireBase.document(it).update(mapOf("avatar" to url)).addOnCompleteListener {
                call(it.isSuccessful)
            }
        }
    }
}