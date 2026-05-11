package com.karunada.vanya.data.model

data class SoundItem(
    val id: Int,
    val name: String,
    val description: String,
    val emoji: String,
    val fileName: String  // name of the audio file in res/raw/
)