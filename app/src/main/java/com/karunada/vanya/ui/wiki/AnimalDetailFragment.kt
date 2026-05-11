package com.karunada.vanya.ui.wiki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.karunada.vanya.R
import com.karunada.vanya.data.AnimalData
import com.karunada.vanya.data.model.WikiItem
import com.karunada.vanya.databinding.FragmentAnimalDetailBinding

class AnimalDetailFragment : Fragment() {

    private var _binding: FragmentAnimalDetailBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseDatabase.getInstance("https://karunadavanya-1ad24-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("wiki")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wikiId = arguments?.getString("wikiId") ?: return

        if (wikiId.startsWith("local_")) {
            val id = wikiId.replace("local_", "").toIntOrNull() ?: return
            val animal = AnimalData.getAnimals().find { it.id == id } ?: return
            
            // Map Animal to WikiItem for display
            val item = WikiItem(
                id = wikiId,
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
            displayWiki(item)
        } else {
            db.child(wikiId).get().addOnSuccessListener { snapshot ->
                if (_binding == null) return@addOnSuccessListener
                val item = snapshot.getValue(WikiItem::class.java) ?: return@addOnSuccessListener
                displayWiki(item)
            }
        }
    }

    private fun displayWiki(item: WikiItem) {
        with(binding) {
            if (item.imageUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(item.imageUrl)
                    .placeholder(R.drawable.app_logo)
                    .into(detailImage)
            } else if (item.imageRes != 0) {
                detailImage.setImageResource(item.imageRes)
            } else {
                detailImage.setImageResource(R.drawable.app_logo)
            }
                
            detailName.text = item.name
            detailScientific.text = item.scientificName
            detailCategory.text = item.category
            detailDescription.text = item.description
            
            // Do's and Don'ts
            labelDos.text = if (item.id.startsWith("local_")) "Did you know?" else "Safety Do's"
            detailFunFact.text = item.dos
            
            labelDonts.text = if (item.id.startsWith("local_")) "Threats & Conservation" else "Safety Don'ts"
            detailThreat.text = item.donts
            
            // Show habitat info if available (for local items)
            if (item.habitat.isNotEmpty()) {
                detailHabitat.visibility = View.VISIBLE
                detailHabitat.text = "Habitat: ${item.habitat}"
            } else {
                detailHabitat.visibility = View.GONE
            }

            if (item.diet.isNotEmpty()) {
                detailDiet.visibility = View.VISIBLE
                detailDiet.text = "Diet: ${item.diet}"
            } else {
                detailDiet.visibility = View.GONE
            }

            if (item.lifespan.isNotEmpty()) {
                detailLifespan.visibility = View.VISIBLE
                detailLifespan.text = "Lifespan: ${item.lifespan}"
            } else {
                detailLifespan.visibility = View.GONE
            }

            if (item.status.isNotEmpty()) {
                detailStatus.visibility = View.VISIBLE
                detailStatus.text = "Status: ${item.status}"
            } else {
                detailStatus.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}