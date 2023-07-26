package com.gps.phonetracker.numberlocator.family.tracklocation.model

import java.io.Serializable

data class Users(
    val id: String,
    val title: String,
    val avatar: String,
    val pin: String,
    val speed: String,
    val isShare: Boolean,
    val latitude: Double,
    val longitude: Double,
    val friends: List<String>,
    val sentFriendRequest: List<String>,
    val receivedFriendRequest: List<String>,

    ) : Serializable {
    constructor() : this("", "", "", "", "", false, 0.0, 0.0, emptyList(), emptyList(), emptyList())
}