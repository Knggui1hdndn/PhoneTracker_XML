package com.gps.phonetracker.numberlocator.family.tracklocation.model

import java.io.Serializable

data class Users(
    val id: String,
    val title: String,
    val avatar: String,
    val phoneNumber: String,
    val pass: String,
    val pin: String,
    val speed: String,
    val isShare: Boolean,
    val latitude: Double,
    val longitude: Double,
    val friends: List<String>,
    val sentFriendRequest: List<String>,
    val receivedFriendRequest: List<String>,

    ) : Serializable {
    constructor() : this("", "", "","","", "", "", false, 0.0, 0.0, emptyList(), emptyList(), emptyList())
    constructor(title:String,phoneNumber:String,pass:String,avatar: String) : this("", title, avatar,phoneNumber,pass, "", "", false, 0.0, 0.0, emptyList(), emptyList(), emptyList())
 }