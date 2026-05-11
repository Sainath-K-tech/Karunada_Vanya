package com.karunada.vanya.ui.alert

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karunada.vanya.data.model.Alert
import com.karunada.vanya.databinding.ItemAlertBinding
import java.io.IOException

class AlertAdapter(
    private var alerts: List<Alert>
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingUrl: String? = null

    class AlertViewHolder(val binding: ItemAlertBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        with(holder.binding) {
            alertType.text = when {
                !alert.videoUrl.isNullOrEmpty() -> "📹 Video Alert"
                !alert.imageUrl.isNullOrEmpty() -> "🖼️ Photo Alert"
                !alert.audioUrl.isNullOrEmpty() -> "🔊 Voice Alert"
                else -> "⚠️ Wildlife Alert"
            }
            alertLocation.text = "📍 ${alert.location}"
            
            if (alert.description.isEmpty()) {
                alertDescription.visibility = View.GONE
            } else {
                alertDescription.visibility = View.VISIBLE
                alertDescription.text = alert.description
            }
            
            alertReporter.text = "Reported by ${alert.reporterName} · ${alert.timeAgo()}"

            // Image handling
            if (!alert.imageUrl.isNullOrEmpty()) {
                alertImage.visibility = View.VISIBLE
                Glide.with(root.context).load(alert.imageUrl).into(alertImage)
            } else {
                alertImage.visibility = View.GONE
            }

            // Video handling
            if (!alert.videoUrl.isNullOrEmpty()) {
                btnWatchVideo.visibility = View.VISIBLE
                btnWatchVideo.setOnClickListener {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(alert.videoUrl), "video/*")
                    root.context.startActivity(intent)
                }
            } else {
                btnWatchVideo.visibility = View.GONE
            }

            // Audio Player logic
            if (!alert.audioUrl.isNullOrEmpty()) {
                audioPlayerContainer.visibility = View.VISIBLE
                btnPlayAudio.setOnClickListener {
                    playAudio(alert.audioUrl, holder)
                }
            } else {
                audioPlayerContainer.visibility = View.GONE
            }
        }
    }

    private fun playAudio(url: String, holder: AlertViewHolder) {
        if (currentPlayingUrl == url && mediaPlayer?.isPlaying == true) {
            stopAudio()
            holder.binding.btnPlayAudio.setImageResource(android.R.drawable.ic_media_play)
            return
        }

        stopAudio()
        currentPlayingUrl = url
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { 
                    start() 
                    holder.binding.btnPlayAudio.setImageResource(android.R.drawable.ic_media_pause)
                }
                setOnCompletionListener { 
                    holder.binding.btnPlayAudio.setImageResource(android.R.drawable.ic_media_play)
                    currentPlayingUrl = null
                }
            } catch (e: IOException) {
                Toast.makeText(holder.itemView.context, "Error playing audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentPlayingUrl = null
    }

    fun updateAlerts(newAlerts: List<Alert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }

    override fun getItemCount() = alerts.size
    
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        stopAudio()
    }
}