package com.karunada.vanya.data.model

data class GuideItem(
    val id: Int,
    val animal: String,
    val emoji: String,
    val situation: String,
    val steps: List<String>,
    val doNot: List<String>,
    val backgroundColor: String
)