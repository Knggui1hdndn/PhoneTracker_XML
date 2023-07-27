package com.gps.phonetracker.numberlocator.family.tracklocation.`interface`

import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users

interface InterfaceLogin {
    interface View {
        fun success(s:String)
        fun error(s:String)
    }

    interface Presenter {
        fun login(phoneNumber:String,pass:String)
        fun register(users: Users)
    }
}

