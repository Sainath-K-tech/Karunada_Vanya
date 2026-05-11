package com.karunada.vanya

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.data.model.User
import com.karunada.vanya.databinding.ActivityManageUsersBinding

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageUsersBinding
    private val db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users")

    private var allUsers = mutableListOf<User>()
    private var filteredUsers = mutableListOf<User>()

    private val districts = listOf(
        "All Districts", "Bagalkot", "Ballari", "Belagavi", "Bengaluru Rural", "Bengaluru Urban", 
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupFilters()
        loadUsers()

        binding.btnSearch.setOnClickListener {
            val district = binding.filterDistrict.text.toString()
            val area = binding.filterArea.text.toString()
            applyFilters(district, area)
        }

        // Initially hide result label and list
        binding.resultsLabel.visibility = View.GONE
        binding.usersListView.visibility = View.GONE
    }

    private fun setupFilters() {
        val distAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, districts)
        binding.filterDistrict.setAdapter(distAdapter)

        binding.filterDistrict.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            if (selected == "All Districts") {
                binding.filterArea.setText("All Areas", false)
                binding.filterArea.isEnabled = false
            } else {
                updateAreaFilter(selected)
            }
        }
    }

    private fun updateAreaFilter(district: String) {
        val areaList = mutableListOf("All Areas")
        areaList.addAll(areas[district] ?: listOf("General Area 1", "General Area 2"))
        
        val areaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, areaList)
        binding.filterArea.apply {
            setAdapter(areaAdapter)
            setText("All Areas", false)
            isEnabled = true
        }
    }

    private fun loadUsers() {
        db.get().addOnSuccessListener { snapshot ->
            allUsers.clear()
            for (child in snapshot.children) {
                val user = child.getValue(User::class.java)
                if (user != null && user.phone != "0000000000") {
                    allUsers.add(user)
                }
            }
        }
    }

    private fun applyFilters(district: String, area: String) {
        filteredUsers = allUsers.filter { user ->
            val distMatch = district.isEmpty() || district == "All Districts" || user.district == district
            val areaMatch = area.isEmpty() || area == "All Areas" || user.area == area
            distMatch && areaMatch
        }.toMutableList()

        binding.resultsLabel.visibility = View.VISIBLE
        binding.usersListView.visibility = View.VISIBLE
        binding.resultsLabel.text = "Results: ${filteredUsers.size} Users Found"

        val adapter = object : ArrayAdapter<User>(this, R.layout.item_user_manage, filteredUsers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.item_user_manage, parent, false)
                val user = getItem(position)!!
                
                view.findViewById<TextView>(R.id.userName).text = user.name
                view.findViewById<TextView>(R.id.userPhone).text = "📞 ${user.phone}"
                
                return view
            }
        }
        binding.usersListView.adapter = adapter

        binding.usersListView.setOnItemClickListener { _, _, position, _ ->
            showUserDetailsDialog(filteredUsers[position])
        }

        binding.usersListView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteDialog(filteredUsers[position])
            true
        }
    }

    private fun showUserDetailsDialog(user: User) {
        val message = """
            Name: ${user.name}
            Phone: ${user.phone}
            Role: ${user.role.uppercase()}
            
            District: ${user.district}
            Area/Village: ${user.area}
            
            Joined At: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(user.joinedAt)}
            
            Photo URL: ${if(user.profilePicUrl.isEmpty()) "No photo uploaded" else user.profilePicUrl}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("User Details")
            .setMessage(message)
            .setPositiveButton("Close", null)
            .setNeutralButton("View Photo") { _, _ ->
                if (user.profilePicUrl.isNotEmpty()) {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(user.profilePicUrl))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No photo available", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun showDeleteDialog(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Delete ${user.name} permanently?")
            .setPositiveButton("Delete") { _, _ ->
                db.child(user.phone).removeValue().addOnSuccessListener {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                    loadUsers()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}