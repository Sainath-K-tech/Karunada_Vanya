package com.karunada.vanya.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Alert(
    val id: String = "",
    val type: String = "",
    val location: String = "",
    val description: String = "",
    val reporterName: String = "",
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val timestamp: Long = 0L
) {
    // Check if alert is still active (within 5 hours)
    @Exclude
    fun isActive(): Boolean {
        val fiveHoursInMillis = 5 * 60 * 60 * 1000L
        return System.currentTimeMillis() - timestamp < fiveHoursInMillis
    }

    // How long ago was this reported
    @Exclude
    fun timeAgo(): String {
        val diff = System.currentTimeMillis() - timestamp
        val minutes = diff / (60 * 1000)
        val hours = diff / (60 * 60 * 1000)
        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minutes ago"
            hours < 5 -> "$hours hours ago"
            else -> "Expired"
        }
    }
}
