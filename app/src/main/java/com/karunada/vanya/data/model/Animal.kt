package com.karunada.vanya.data.model

data class Animal(
    val id: Int,
    val name: String,
    val scientificName: String,
    val category: String,
    val shortDescription: String,
    val fullDescription: String,
    val funFact: String,
    val habitat: String,
    val diet: String,
    val lifespan: String,
    val conservationStatus: String,
    val threat: String,
    val imageRes: Int
)