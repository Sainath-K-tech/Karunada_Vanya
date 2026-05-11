package com.karunada.vanya.ui.alert

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karunada.vanya.data.model.Alert
import com.karunada.vanya.databinding.DialogReportAlertBinding
import com.karunada.vanya.databinding.FragmentAlertBinding
import com.karunada.vanya.utils.CloudinaryHelper

class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AlertAdapter
    private val database = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("alerts")

    private var selectedVideoUri: Uri? = null

    private val captureVideo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val videoUri: Uri? = result.data?.data
            if (videoUri != null) {
                selectedVideoUri = videoUri
                Toast.makeText(requireContext(), "Video recorded! Click 'Send' to upload.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedVideoUri = uri
            Toast.makeText(requireContext(), "Video selected! Click 'Send' to upload.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("karunada_prefs", Context.MODE_PRIVATE)
        val role = prefs.getString("role", "user")

        if (role == "offline_user") {
            binding.activeAlertsLabel.text = "Offline Mode: Alerts Disabled"
            binding.reportAlertButton.visibility = View.GONE
            binding.alertRecyclerView.visibility = View.GONE
            return
        }

        adapter = AlertAdapter(emptyList())
        binding.alertRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.alertRecyclerView.adapter = adapter

        listenForAlerts()

        binding.reportAlertButton.setOnClickListener {
            if (checkPermissions()) {
                showReportDialog()
            } else {
                requestPermissions()
            }
        }
    }

    private fun listenForAlerts() {
        val prefs = requireContext().getSharedPreferences("karunada_prefs", Context.MODE_PRIVATE)
        val userDistrict = prefs.getString("district", "") ?: ""
        val userArea = prefs.getString("area", "") ?: ""

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (_binding == null) return

                val alerts = mutableListOf<Alert>()
                for (child in snapshot.children) {
                    val alert = child.getValue(Alert::class.java)
                    if (alert != null && alert.isActive()) {
                        // Filter by District and Area
                        val locationParts = alert.location.split(", ")
                        val alertArea = if (locationParts.isNotEmpty()) locationParts[0] else ""
                        val alertDistrict = if (locationParts.size > 1) locationParts[1] else ""

                        if (alertDistrict == userDistrict && alertArea == userArea) {
                            alerts.add(alert)
                        }
                    }
                }
                alerts.sortByDescending { it.timestamp }
                adapter.updateAlerts(alerts)

                binding.activeAlertsLabel.text =
                    if (alerts.isEmpty()) "No active alerts in $userArea"
                    else "Active Alerts in $userArea (${alerts.size})"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load alerts", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showReportDialog() {
        val dialogBinding = DialogReportAlertBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Send Sighting", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialogBinding.btnRecordVideo.setOnClickListener {
            val intent = android.content.Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE)
            intent.putExtra(android.provider.MediaStore.EXTRA_DURATION_LIMIT, 20)
            intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0) // Quality 0 is safer for mobile uploads
            captureVideo.launch(intent)
        }

        dialogBinding.btnSelectVideo.setOnClickListener {
            pickVideo.launch("video/*")
        }

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (selectedVideoUri == null) {
                    Toast.makeText(requireContext(), "Please record or select a video proof", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                uploadVideoAndSubmit(dialog, dialogBinding)
            }
        }

        dialog.show()
    }

    private fun uploadVideoAndSubmit(dialog: AlertDialog, dialogBinding: DialogReportAlertBinding) {
        val videoUri = selectedVideoUri ?: return
        
        dialogBinding.uploadProgress.visibility = View.VISIBLE
        dialogBinding.attachmentStatus.text = "Uploading video..."
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        
        CloudinaryHelper.uploadMedia(videoUri, "alert_videos", { downloadUrl ->
            submitAlert(downloadUrl)
            dialog.dismiss()
            selectedVideoUri = null
        }, { error ->
            dialogBinding.uploadProgress.visibility = View.GONE
            dialogBinding.attachmentStatus.text = "Upload failed"
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
            android.util.Log.e("AlertFragment", "Upload Failed: $error")
            Toast.makeText(requireContext(), "Upload failed: $error", Toast.LENGTH_LONG).show()
        })
    }

    private fun submitAlert(videoUrl: String) {
        val prefs = requireContext().getSharedPreferences("karunada_prefs", Context.MODE_PRIVATE)
        val reporterName = prefs.getString("name", "Anonymous") ?: "Anonymous"
        val village = prefs.getString("area", "Unknown Location") ?: "Unknown Location"
        val district = prefs.getString("district", "") ?: ""
        
        val locationLabel = if (district.isNotEmpty()) "$village, $district" else village

        val alertId = database.push().key ?: return
        val alert = Alert(
            id = alertId,
            type = "Video Sighting",
            location = locationLabel,
            description = "Wildlife sighting reported with video proof.",
            reporterName = reporterName,
            videoUrl = videoUrl,
            timestamp = System.currentTimeMillis()
        )
        database.child(alertId).setValue(alert)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Wildlife sighting alert sent!", Toast.LENGTH_SHORT).show()
                cleanupOldAlerts()
            }
    }

    private fun cleanupOldAlerts() {
        val cutoff = System.currentTimeMillis() - (5 * 60 * 60 * 1000L)
        database.orderByChild("timestamp").endAt(cutoff.toDouble()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    child.ref.removeValue()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkPermissions(): Boolean {
        val audioOk = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        val cameraOk = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return audioOk && cameraOk
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        ), 1001)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}