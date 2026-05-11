package com.karunada.vanya.data

import com.karunada.vanya.data.model.SoundItem

object SoundData {
    fun getSounds(): List<SoundItem> = listOf(
        SoundItem(1, "Indian Elephant", "Trumpeting call of a wild elephant", "🐘", "elephant"),
        SoundItem(2, "Bengal Tiger", "Roar of the Bengal Tiger", "🐯", "tiger"),
        SoundItem(3, "Indian Peacock", "Call of the national bird of India", "🦚", "peacock"),
        SoundItem(4, "Great Hornbill", "Loud calls of the Great Hornbill", "🦜", "hornbill"),
        SoundItem(5, "Forest Rain", "Rain sounds from the Western Ghats", "🌧️", "forest_rain"),
        SoundItem(6, "Indian Jungle", "Ambient sounds of Karnataka jungle at dawn", "🌳", "jungle_dawn")
    )
}