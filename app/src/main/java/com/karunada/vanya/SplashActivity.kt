package com.karunada.vanya

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("karunada_prefs", MODE_PRIVATE)
        val savedPhone = prefs.getString("phone", null)

        if (savedPhone == null) {
            // First time → go to Login
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // Already logged in → go to Main
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}