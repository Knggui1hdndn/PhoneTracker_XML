package com.gps.phonetracker.numberlocator.family.tracklocation.utili

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object ShareData {
    var uid= "23orf928pwVVBXhGrcwx"
    var isShare=false
    fun Toast(context: Context,mes:String){
        Toast.makeText(context,mes,Toast.LENGTH_SHORT).show()
    }
}