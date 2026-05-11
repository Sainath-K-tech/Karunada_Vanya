package com.karunada.vanya

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.data.model.Alert
import com.karunada.vanya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val startTime = System.currentTimeMillis()
    private val alertsDb = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("alerts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        
        val prefs = getSharedPreferences("karunada_prefs", MODE_PRIVATE)
        val role = prefs.getString("role", "user")

        // 1. Setup Navigation Graph dynamically based on role
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        when (role) {
            "main_admin" -> navGraph.setStartDestination(R.id.adminFragment)
            "offline_user" -> navGraph.setStartDestination(R.id.wikiFragment)
            else -> navGraph.setStartDestination(R.id.wikiFragment)
        }
        navController.graph = navGraph

        // 2. Setup Bottom Nav with Controller
        binding.bottomNav.setupWithNavController(navController)

        // 3. Hide menu items based on role
        applyMenuVisibility(role)

        // 4. Start listening for alerts (all roles except offline mode should see alerts)
        if (role != "offline_user") {
            startAlertListener()
        }
    }

    private fun startAlertListener() {
        val prefs = getSharedPreferences("karunada_prefs", MODE_PRIVATE)
        val userDistrict = prefs.getString("district", "")?.trim() ?: ""
        val userArea = prefs.getString("area", "")?.trim() ?: ""

        alertsDb.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val alert = snapshot.getValue(Alert::class.java)
                
                if (alert != null && alert.timestamp > startTime) {
                    val locationParts = alert.location.split(",")
                    val alertArea = locationParts.getOrNull(0)?.trim() ?: ""
                    val alertDistrict = locationParts.getOrNull(1)?.trim() ?: ""

                    // Robust matching (Case-insensitive and trimmed)
                    if (alertDistrict.equals(userDistrict, ignoreCase = true) && 
                        alertArea.equals(userArea, ignoreCase = true)) {
                        triggerAlertEffects(alert)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun triggerAlertEffects(alert: Alert) {
        // Show Toast
        Toast.makeText(this, "⚠️ WILDLIFE ALERT: ${alert.type.uppercase()} seen at ${alert.location}", Toast.LENGTH_LONG).show()

        // 1. Buzz Sound (High pitch tone) - 10 Seconds
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGen.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 10000) 
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Vibration - 10 Seconds
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Pattern for ~10 seconds (Vibrate 1.2s, Pause 0.2s, x7)
                val pattern = longArrayOf(0, 1200, 200, 1200, 200, 1200, 200, 1200, 200, 1200, 200, 1200, 200, 1200)
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(10000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyMenuVisibility(role: String?) {
        val menu = binding.bottomNav.menu
        
        // Default visibility (Reset)
        menu.findItem(R.id.wikiFragment)?.isVisible = true
        menu.findItem(R.id.alertFragment)?.isVisible = true
        menu.findItem(R.id.guideFragment)?.isVisible = true
        menu.findItem(R.id.profileFragment)?.isVisible = true
        menu.findItem(R.id.adminFragment)?.isVisible = false

        when (role) {
            "offline_user" -> {
                menu.findItem(R.id.alertFragment)?.isVisible = false
                menu.findItem(R.id.adminFragment)?.isVisible = false
            }
            "main_admin" -> {
                menu.findItem(R.id.wikiFragment)?.isVisible = false
                menu.findItem(R.id.alertFragment)?.isVisible = false
                menu.findItem(R.id.guideFragment)?.isVisible = false
                menu.findItem(R.id.adminFragment)?.isVisible = true
            }
            "admin" -> {
                menu.findItem(R.id.adminFragment)?.isVisible = true
            }
            else -> {
                menu.findItem(R.id.adminFragment)?.isVisible = false
            }
        }
    }
}
