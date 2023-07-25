package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import android.graphics.Bitmap
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

interface InterfaceMaine {
    interface View{
        fun onChangeData(users: MutableList<Users>)
    }
    interface Presenter{
        fun setOnChangeData()
        fun putLocation(users: Users)
       // fun setContentMarker(pin:String,speed:String,imgUrl:String,load:(Bitmap)->Unit)
    }
}