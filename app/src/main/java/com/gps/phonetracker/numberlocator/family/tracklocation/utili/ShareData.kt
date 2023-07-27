package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.content.Context
import android.widget.Toast
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

object ShareData {
    var currentUser: Users? = null
    var uid = if (currentUser != null) currentUser?.id else ""
    const val URL_Avatar_Default ="https://firebasestorage.googleapis.com/v0/b/du-an-1-12449.appspot.com/o/image%2Fdefault.jpg?alt=media&token=ee524037-8d88-4238-a134-7c3af6ee1414"
    var isShare = false
    fun Toast(context: Context, mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }
}