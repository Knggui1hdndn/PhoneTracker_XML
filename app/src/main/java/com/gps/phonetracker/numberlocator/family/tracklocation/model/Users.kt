package com.gps.phonetracker.numberlocator.family.tracklocation.model

import java.io.Serializable

data class Users(
    val id: Int,
    val title:String,
    val avatar: String,
    val pin: String,
    val speed: String,
    val isShare: Boolean,
    val latitude: Double,
    val longitude: Double
):Serializable {
    constructor() : this(0, "","", "", "", false,  0.0, 0.0)
}