package com.gps.phonetracker.numberlocator.family.tracklocation.firebase

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import org.mindrot.jbcrypt.BCrypt

class AccountFireBase : InterfaceFireBase.Presenter.Account {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    private val storageRef = FirebaseStorage.getInstance().reference.child("image")
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
        fireBase.document(id).update(mapOf("id" to id,"friends" to listOf(id))).addOnCompleteListener {
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
                        }else{
                            call(null, "Error")
                        }
                    }
                }else{
                    call(null, "Error")
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
}