package com.karunada.vanya

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.data.model.User
import com.karunada.vanya.databinding.ActivityRegisterBinding
import com.karunada.vanya.utils.CloudinaryHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users")

    private var selectedImageUri: Uri? = null

    private val districts = listOf(
        "Bagalkot", "Ballari", "Belagavi", "Bengaluru Rural", "Bengaluru Urban", 
        "Bidar", "Chamarajanagar", "Chikkaballapur", "Chikkamagaluru", "Chitradurga", 
        "Dakshina Kannada", "Davanagere", "Dharwad", "Gadag", "Hassan", "Haveri", 
        "Kalaburagi", "Kodagu", "Kolar", "Koppal", "Mandya", "Mysuru", "Raichur", 
        "Ramanagara", "Shivamogga", "Tumakuru", "Udupi", "Uttara Kannada", "Vijayapura", "Vijayanagara", "Yadgir"
    ).sorted()

    private val areas = mapOf(
        "Bagalkot" to listOf("Bagalkot City", "Badami", "Jamkhandi", "Mudhol", "Ilkal", "Guledgudda", "Mahalingpur", "Banahatti"),
        "Ballari" to listOf("Ballari City", "Sandur", "Siruguppa", "Kampli", "Kurugodu"),
        "Belagavi" to listOf("Belagavi City", "Gokak", "Athani", "Chikkodi", "Bailhongal", "Saundatti", "Ramdurg", "Nippani"),
        "Bengaluru Rural" to listOf("Devanahalli", "Doddaballapura", "Hoskote", "Nelamangala"),
        "Bengaluru Urban" to listOf("RT Nagar", "Hebbal", "Majestic", "Jayanagar", "Indiranagar", "Whitefield", "Yelahanka", "Banashankari", "Electronic City", "Koramangala", "BTM Layout", "Rajajinagar"),
        "Bidar" to listOf("Bidar City", "Bhalki", "Humnabad", "Basavakalyan", "Aurad"),
        "Chamarajanagar" to listOf("Chamarajanagar", "Kollegal", "Gundlupet", "Hanur", "Yelandur"),
        "Chikkaballapur" to listOf("Chikkaballapur", "Chintamani", "Gauribidanur", "Sidlaghatta", "Bagepalli"),
        "Chikkamagaluru" to listOf("Chikkamagaluru", "Kadur", "Koppa", "Mudigere", "Tarikere", "Sringeri", "Narasimharajapura"),
        "Chitradurga" to listOf("Chitradurga", "Hiriyur", "Hosadurga", "Holalkere", "Molakalmuru", "Challakere"),
        "Dakshina Kannada" to listOf("Mangaluru", "Ullal", "Bantwal", "Puttur", "Sulya", "Belthangady", "Moodabidri"),
        "Davanagere" to listOf("Davanagere", "Harihar", "Honnali", "Channagiri", "Jagalur"),
        "Dharwad" to listOf("Hubballi", "Dharwad City", "Navalgund", "Kalghatgi", "Kundgol"),
        "Gadag" to listOf("Gadag-Betageri", "Mundargi", "Ron", "Shirahatti", "Gajendragad", "Lakshmeshwar"),
        "Hassan" to listOf("Hassan City", "Arsikere", "Channarayapatna", "Holenarasipura", "Sakleshpura", "Belur", "Alur", "Arkalgud"),
        "Haveri" to listOf("Haveri City", "Byadgi", "Hangal", "Ranebennur", "Savanur", "Shiggaon", "Hirekerur"),
        "Kalaburagi" to listOf("Kalaburagi City", "Afzalpur", "Aland", "Chincholi", "Chittapur", "Sedam", "Shahabad", "Jevargi"),
        "Kodagu" to listOf("Madikeri", "Virajpet", "Kushalnagar", "Somwarpet", "Gonikoppal"),
        "Kolar" to listOf("Kolar City", "Bangarapet", "Malur", "Mulbagal", "Srinivaspur", "KGF"),
        "Koppal" to listOf("Koppal", "Gangavathi", "Kushtagi", "Yelburga", "Kanakagiri"),
        "Mandya" to listOf("Mandya City", "Maddur", "Malavalli", "Pandavapura", "Srirangapatna", "Nagamangala", "Krishnarajapet"),
        "Mysuru" to listOf("Kuvempunagar", "Siddhartha Layout", "Gokulam", "Vidyaranyapuram", "Hunsur", "Nanjangud", "Periyapatna", "T.Narsipura", "Bannur", "K.R. Nagar"),
        "Raichur" to listOf("Raichur City", "Sindhanur", "Manvi", "Devadurga", "Lingsugur", "Maski"),
        "Ramanagara" to listOf("Ramanagara", "Channapatna", "Kanakapura", "Magadi"),
        "Shivamogga" to listOf("Shivamogga City", "Bhadravathi", "Sagar", "Shikaripura", "Soraba", "Thirthahalli", "Hosanagara"),
        "Tumakuru" to listOf("Tumakuru City", "Chikkanayakanahalli", "Kunigal", "Madhugiri", "Sira", "Tiptur", "Gubbi", "Pavagada", "Turuvekere"),
        "Udupi" to listOf("Udupi", "Kundapura", "Karkala", "Brahmavara", "Kaup", "Byndoor"),
        "Uttara Kannada" to listOf("Karwar", "Sirsi", "Dandeli", "Kumta", "Bhatkal", "Honnavar", "Yellapur", "Ankola", "Haliyal", "Joida"),
        "Vijayapura" to listOf("Vijayapura City", "Indi", "Muddebihal", "Sindgi", "Babaleshwar", "Basavana Bagewadi", "Talikoti"),
        "Vijayanagara" to listOf("Hosapete", "Kotturu", "Hagaribommanahalli", "Harapanahalli", "Hoovina Hadagali", "Kudligi"),
        "Yadgir" to listOf("Yadgir", "Shahapur", "Shorapur", "Gurmitkal")
    )

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.profileImage.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdowns()

        binding.btnSelectPhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.registerButton.setOnClickListener {
            val name = binding.inputName.text.toString().trim()
            val phone = binding.inputPhone.text.toString().trim()
            val district = binding.inputDistrict.text.toString().trim()
            val area = binding.inputArea.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || district.isEmpty() || area.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length != 10) {
                Toast.makeText(this, "Enter 10-digit phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkAndRegister(name, phone, district, area)
        }

        binding.loginLink.setOnClickListener { finish() }
    }

    private fun setupDropdowns() {
        val list = mutableListOf("Select District")
        list.addAll(districts)
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
        binding.inputDistrict.setAdapter(districtAdapter)

        binding.inputDistrict.setOnItemClickListener { parent, _, position, _ ->
            val selectedDistrict = parent.getItemAtPosition(position) as String
            if (selectedDistrict != "Select District") {
                updateAreaDropdown(selectedDistrict)
            }
        }
    }

    private fun updateAreaDropdown(district: String) {
        val areaList = areas[district] ?: listOf("General Area 1", "General Area 2")
        val areaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, areaList)
        binding.inputArea.apply {
            setAdapter(areaAdapter)
            setText("") 
            isEnabled = true
        }
    }

    private fun checkAndRegister(name: String, phone: String, district: String, area: String) {
        db.child(phone).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                Toast.makeText(this, "Phone already registered. Please login.", Toast.LENGTH_SHORT).show()
            } else {
                if (selectedImageUri != null) {
                    uploadPhotoAndRegister(name, phone, district, area)
                } else {
                    saveUser(name, phone, district, area, "")
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPhotoAndRegister(name: String, phone: String, district: String, area: String) {
        val uri = selectedImageUri ?: return
        Toast.makeText(this, "Uploading profile photo...", Toast.LENGTH_SHORT).show()
        
        CloudinaryHelper.uploadMedia(
            uri,
            "profile_photos",
            onSuccess = { downloadUrl ->
                saveUser(name, phone, district, area, downloadUrl)
            },
            onError = { error ->
                Toast.makeText(this, "Photo upload failed: $error. Saving without photo.", Toast.LENGTH_SHORT).show()
                saveUser(name, phone, district, area, "")
            }
        )
    }

    private fun saveUser(
        name: String, phone: String,
        district: String, area: String, photoUrl: String
    ) {
        val uid = db.push().key ?: ""
        val user = User(
            uid = uid,
            name = name,
            phone = phone,
            district = district,
            area = area,
            profilePicUrl = photoUrl,
            role = "user",
            joinedAt = System.currentTimeMillis()
        )

        db.child(phone).setValue(user)
            .addOnSuccessListener {
                getSharedPreferences("karunada_prefs", MODE_PRIVATE)
                    .edit()
                    .putString("phone", phone)
                    .putString("name", name)
                    .putString("district", district)
                    .putString("area", area)
                    .putString("role", "user")
                    .putString("uid", uid)
                    .putString("photo", photoUrl)
                    .apply()

                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Save Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}