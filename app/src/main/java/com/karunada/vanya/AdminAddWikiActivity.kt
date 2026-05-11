package com.karunada.vanya

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.data.model.WikiItem
import com.karunada.vanya.databinding.ActivityAdminAddWikiBinding
import com.karunada.vanya.utils.CloudinaryHelper

class AdminAddWikiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddWikiBinding
    private val db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("wiki")

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            uploadWikiPhoto(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddWikiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUploadWikiPhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSubmitWiki.setOnClickListener {
            validateAndSave()
        }
    }

    private fun uploadWikiPhoto(uri: Uri) {
        Toast.makeText(this, "Uploading photo...", Toast.LENGTH_SHORT).show()
        CloudinaryHelper.uploadMedia(
            uri,
            "wiki_photos",
            onSuccess = { downloadUrl ->
                binding.inputImageUrl.setText(downloadUrl)
                Toast.makeText(this, "Photo uploaded!", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                Toast.makeText(this, "Upload failed: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun validateAndSave() {
        val url = binding.inputImageUrl.text.toString().trim()
        val name = binding.inputAnimalName.text.toString().trim()
        val scientific = binding.inputScientificName.text.toString().trim()
        val desc = binding.inputDescription.text.toString().trim()
        val dos = binding.inputDos.text.toString().trim()
        val donts = binding.inputDonts.text.toString().trim()

        if (name.isEmpty() || desc.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "Photo URL, Name and Description are required", Toast.LENGTH_SHORT).show()
            return
        }

        saveWiki(name, scientific, desc, dos, donts, url)
    }

    private fun saveWiki(name: String, scientific: String, desc: String, dos: String, donts: String, url: String) {
        val id = db.push().key ?: ""
        val item = WikiItem(
            id = id,
            name = name,
            scientificName = scientific,
            description = desc,
            dos = dos,
            donts = donts,
            imageUrl = url,
            timestamp = System.currentTimeMillis()
        )
        
        db.child(id).setValue(item).addOnSuccessListener {
            Toast.makeText(this, "Wiki content added successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save to database", Toast.LENGTH_SHORT).show()
        }
    }
}