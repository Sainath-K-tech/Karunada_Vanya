package com.karunada.vanya.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class WikiItem(
    val id: String = "",
    val name: String = "",
    val scientificName: String = "",
    val description: String = "",
    val dos: String = "",
    val donts: String = "",
    val imageUrl: String = "",
    val imageRes: Int = 0, // For local animals
    val category: String = "Wildlife",
    val habitat: String = "",
    val diet: String = "",
    val lifespan: String = "",
    val status: String = "",
    val timestamp: Long = 0L
)