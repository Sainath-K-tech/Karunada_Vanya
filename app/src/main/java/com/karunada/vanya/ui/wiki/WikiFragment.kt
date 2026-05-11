package com.karunada.vanya.ui.wiki

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karunada.vanya.R
import com.karunada.vanya.data.AnimalData
import com.karunada.vanya.data.model.WikiItem
import com.karunada.vanya.databinding.FragmentWikiBinding

class WikiFragment : Fragment() {
    private var _binding: FragmentWikiBinding? = null
    private val binding get() = _binding!!
    
    private val db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("wiki")
    private lateinit var adapter: WikiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWikiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("karunada_prefs", android.content.Context.MODE_PRIVATE)
        val role = prefs.getString("role", "user")
        
        if (role == "offline_user") {
            binding.wikiHeaderTitle.text = "📚 Education Mode"
            binding.wikiHeaderSubtitle.text = "Animal Wiki & Safety Guides (Offline)"
            binding.btnSounds.visibility = View.GONE
        }

        binding.btnSounds.setOnClickListener {
            findNavController().navigate(R.id.soundsFragment)
        }

        adapter = WikiAdapter(getInitialData()) { wikiItem ->
            val bundle = Bundle().apply {
                putString("wikiId", wikiItem.id)
            }
            findNavController().navigate(R.id.action_wiki_to_detail, bundle)
        }

        binding.animalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.animalRecyclerView.adapter = adapter

        loadWikiContent()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getInitialData(): List<WikiItem> {
        // Convert hardcoded Animal data to WikiItem format
        return AnimalData.getAnimals().map { animal ->
            WikiItem(
                id = "local_${animal.id}",
                name = animal.name,
                scientificName = animal.scientificName,
                description = animal.fullDescription,
                dos = animal.funFact,
                donts = animal.threat,
                imageUrl = "",
                imageRes = animal.imageRes,
                category = animal.category,
                habitat = animal.habitat,
                diet = animal.diet,
                lifespan = animal.lifespan,
                status = animal.conservationStatus
            )
        }
    }

    private fun loadWikiContent() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (_binding == null) return
                
                // Start with initial hardcoded data
                val combinedItems = getInitialData().toMutableList()
                
                // Add new data from Firebase
                for (child in snapshot.children) {
                    child.getValue(WikiItem::class.java)?.let { combinedItems.add(it) }
                }
                
                // Sort by name or timestamp if needed
                adapter.updateData(combinedItems)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load wiki updates", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}