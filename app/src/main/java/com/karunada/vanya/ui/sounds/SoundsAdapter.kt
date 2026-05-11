package com.karunada.vanya.ui.sounds

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karunada.vanya.data.model.SoundItem
import com.karunada.vanya.databinding.ItemSoundBinding

class SoundsAdapter(
    private val sounds: List<SoundItem>,
    private val context: Context
) : RecyclerView.Adapter<SoundsAdapter.SoundViewHolder>() {

    private var currentlyPlaying: MediaPlayer? = null
    private var currentlyPlayingId: Int = -1

    inner class SoundViewHolder(val binding: ItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val binding = ItemSoundBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val sound = sounds[position]
        with(holder.binding) {
            soundEmoji.text = sound.emoji
            soundName.text = sound.name
            soundDescription.text = sound.description

            val isPlaying = currentlyPlayingId == sound.id
            playButton.text = if (isPlaying) "⏹" else "▶"
            soundStatus.text = if (isPlaying) "Playing..." else "Tap to play"

            playButton.setOnClickListener {
                if (currentlyPlayingId == sound.id) {
                    stopSound()
                } else {
                    playSound(sound, position)
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun playSound(sound: SoundItem, position: Int) {
        stopSound()
        val resId = context.resources.getIdentifier(
            sound.fileName, "raw", context.packageName
        )
        if (resId != 0) {
            currentlyPlaying = MediaPlayer.create(context, resId)
            currentlyPlaying?.start()
            currentlyPlayingId = sound.id
            currentlyPlaying?.setOnCompletionListener {
                currentlyPlayingId = -1
                notifyDataSetChanged()
            }
        }
    }

    private fun stopSound() {
        currentlyPlaying?.stop()
        currentlyPlaying?.release()
        currentlyPlaying = null
        currentlyPlayingId = -1
    }

    fun releasePlayer() {
        stopSound()
    }

    override fun getItemCount() = sounds.size
}