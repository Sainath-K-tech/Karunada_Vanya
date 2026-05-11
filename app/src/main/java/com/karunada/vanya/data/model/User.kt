package com.karunada.vanya.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val district: String = "",
    val area: String = "",
    val profilePicUrl: String = "",
    val role: String = "user",
    val joinedAt: Long = 0L
)
