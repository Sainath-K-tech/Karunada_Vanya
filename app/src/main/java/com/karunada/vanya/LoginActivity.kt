package com.karunada.vanya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.data.model.User
import com.karunada.vanya.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DB safely
        try {
            db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users")
        } catch (e: Exception) {
            Log.e("LoginActivity", "Firebase Init Error", e)
        }

        binding.loginButton.setOnClickListener {
            val phone = binding.inputPhone.text.toString().trim()
            if (phone.isEmpty()) {
                Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone == "0000000000") {
                loginAsMainAdmin()
            } else {
                loginUser(phone)
            }
        }

        binding.offlineButton.setOnClickListener {
            Log.d("LoginActivity", "Offline button clicked")
            showEducationPurposeDialog()
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showEducationPurposeDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Education Purpose")
            .setMessage("Are you accessing this app for educational purposes? Offline mode provides wildlife wiki and safety guides without requiring internet.")
            .setPositiveButton("Yes, Proceed") { _, _ ->
                loginAsOfflineUser()
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(true)
            .show()
    }

    private fun loginAsOfflineUser() {
        Log.d("LoginActivity", "Logging in as offline user")
        getSharedPreferences("karunada_prefs", MODE_PRIVATE)
            .edit()
            .putString("phone", "OFFLINE")
            .putString("name", "Student / Guest")
            .putString("role", "offline_user")
            .apply()

        Toast.makeText(this, "Welcome to Offline Education Mode!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loginAsMainAdmin() {
        getSharedPreferences("karunada_prefs", MODE_PRIVATE)
            .edit()
            .putString("phone", "0000000000")
            .putString("name", "Main Admin")
            .putString("role", "main_admin")
            .apply()

        Toast.makeText(this, "Welcome Main Admin!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loginUser(phone: String) {
        if (!::db.isInitialized) {
            Toast.makeText(this, "Database not ready. Check internet.", Toast.LENGTH_SHORT).show()
            return
        }
        
        db.child(phone).get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                Toast.makeText(this, "User $phone not found. Please register.", Toast.LENGTH_LONG).show()
            } else {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val role = snapshot.child("role").getValue(String::class.java) ?: "user"
                    
                    getSharedPreferences("karunada_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("phone", user.phone)
                        .putString("name", user.name)
                        .putString("district", user.district)
                        .putString("area", user.area)
                        .putString("role", role)
                        .putString("uid", user.uid)
                        .putString("photo", user.profilePicUrl)
                        .apply()

                    Toast.makeText(this, "Welcome back, ${user.name} ($role)!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
