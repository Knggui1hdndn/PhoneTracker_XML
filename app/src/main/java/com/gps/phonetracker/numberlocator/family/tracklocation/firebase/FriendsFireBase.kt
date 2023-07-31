package com.gps.phonetracker.numberlocator.family.tracklocation.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class FriendsFireBase(
    private val viewF: InterfaceFireBase.View.Friends?,
    private val view: InterfaceFireBase.View.LiveLocation?
) :
    InterfaceFireBase.Presenter.Friends {
    private val fireBase = FirebaseFirestore.getInstance().collection("users")
    private val fireBase1 = FirebaseFirestore.getInstance().collection("users")

    private fun getListByFriendRequest(
        friendRequests: List<String>,
        onSuccess: (List<Users>) -> Unit
    ) {
        getUsesCurrent { currentUser ->
            if (currentUser == null) {
                viewF?.onError("Xảy ra lỗi")
                return@getUsesCurrent
            }
            if (friendRequests.isNotEmpty()) fireBase.whereIn("id", friendRequests).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(task.result.toObjects(Users::class.java))
                    } else {
                        viewF?.onError(task.exception.toString())
                    }
                }
            else viewF?.onError("Không cos")
        }
    }

    override fun getListReceivedFriendRequest() {
        getListByFriendRequest(
            ShareData.currentUser!!.receivedFriendRequest
        ) { usersList ->
            viewF?.onSuccess(usersList)
        }
    }

    override fun getListSentFriendRequest() {
        getListByFriendRequest(ShareData.currentUser!!.sentFriendRequest) { usersList ->
            viewF?.onSuccess(usersList)
        }
    }

    override fun search(string: String) {
        fireBase.whereEqualTo("phoneNumber", string).get().addOnCompleteListener {
            if (it.isSuccessful) {
                viewF!!.onSuccess(it.result.toObjects(Users::class.java))
            } else {
                viewF!!.onError(it.exception.toString())
            }
        }
    }

    override fun addFriends(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            updateFriendByField(uid, "friends", it.friends)

        }
    }

    override fun addSentFriendRequest(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            val list = it.sentFriendRequest.toMutableList()
            list.add(uid)
            updateFriendByField(uid, "sentFriendRequest", list)
        }
    }

    override fun addReceivedFriendRequest(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            val list = it.sentFriendRequest.toMutableList()
            list.add(uid)
            updateFriendByField(uid, "receivedFriendRequest", list)
        }
    }

    override fun removeFriends(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            val list = it.friends.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "sentFriendRequest", list.toList())
        }
    }

    override fun removeReceivedFriendRequest(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            val list = it.receivedFriendRequest.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "receivedFriendRequest", list.toList())
        }
    }

    override fun removeSentFriendRequest(uid: String) {
        getUsesCurrent {
            if (it == null) {
                return@getUsesCurrent
            }
            val list = it.sentFriendRequest.toMutableList()
            list.remove(uid)
            updateFriendByField(uid, "sentFriendRequest", list.toList())
        }
    }

    override fun getListFriends() {
        getUsesCurrent {
            if (it != null && it.friends.isNotEmpty()) {
                fireBase1.whereIn("id", it.friends).get().addOnSuccessListener {
                    if (it != null) {
                        val users = it.toObjects(Users::class.java)
                        view?.onChangeData(users)
                        viewF?.onSuccess(users)
                    }
                }
            } else {
                viewF?.onError("error")
            }
        }
    }

    private fun updateFriendByField(
        uid: String,
        field: String,
        list: List<String>
    ) {
        fireBase.document(uid).update(mapOf(field to list)).addOnSuccessListener {
            when (field) {
                "friends" -> {
                    removeReceivedFriendRequest(ShareData.uid)
                    removeSentFriendRequest(uid)
                }
            }
        }.addOnFailureListener {}
    }

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
}