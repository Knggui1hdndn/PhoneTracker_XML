package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

interface InterfaceFireBase {
    interface View {
        interface LiveLocation {
            fun onChangeData(users: MutableList<Users>)
        }

        interface Friends {
            fun onSuccess(result: List<Users>)
            fun onError(e: String)
        }
    }

    interface Presenter {
        interface Friends {
            fun getListReceivedFriendRequest()

            fun getListSentFriendRequest()

            fun search(string: String)

            fun addFriends(uid: String)

            fun addSentFriendRequest(uid: String)

            fun addReceivedFriendRequest(uid: String)

            fun removeFriends(uid: String)

            fun removeSentFriendRequest(uid: String)

            fun removeReceivedFriendRequest(uid: String)

            fun getListFriends()

            fun getUsesCurrent(call: (Users?) -> Unit)
        }

        interface Account {
            fun checkPhoneNumberNotExist(phoneNumber: String, call: (Boolean) -> Unit)

            fun register(users: Users, call: (Boolean, String?) -> Unit)

            fun updateImage(url: String, call: (Boolean) -> Unit)

            fun login(phoneNumber: String, pass: String, call: (Users?, String?) -> Unit)

            fun putImage(uri: Uri, call: (String?) -> Unit)

            fun updateIdUserWithIdDocument(id: String, call: (Boolean) -> Unit)
        }

        interface LiveLocation {
            fun onShare(boolean: Boolean, call: (Boolean) -> Unit)

            fun setOnChangeData(uid: String, call: (Users) -> Unit)

            fun putLocation(latLng: LatLng, speed: String)

            fun putPin(pin: String)
        }
    }
}