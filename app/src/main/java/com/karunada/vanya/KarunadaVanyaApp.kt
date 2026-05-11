package com.karunada.vanya

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.firebase.database.FirebaseDatabase

class KarunadaVanyaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase Database with the correct region URL
        FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").setPersistenceEnabled(true)

        // Initialize Cloudinary
        // NOTE: Replace these with your actual Cloudinary credentials
        val config = mapOf(
            "cloud_name" to "dg8irojob", // Placeholder, user can update
            "secure" to true
        )
        MediaManager.init(this, config)
    }
}
