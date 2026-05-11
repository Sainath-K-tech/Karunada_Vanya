package com.karunada.vanya.ui.wiki

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karunada.vanya.data.model.Animal
import com.karunada.vanya.databinding.ItemAnimalBinding

class AnimalAdapter(
    private var animals: List<Animal>,
    private val onAnimalClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    private var fullList: List<Animal> = animals

    inner class AnimalViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding = ItemAnimalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        with(holder.binding) {
            animalName.text = animal.name
            scientificName.text = animal.scientificName
            animalCategory.text = animal.category
            animalImage.setImageResource(animal.imageRes)
            animalImage.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP

            val badgeColor = when (animal.category) {
                "Mammal" -> "#1565C0"
                "Bird" -> "#6A1B9A"
                "Reptile" -> "#BF360C"
                "Plant" -> "#2E7D32"
                else -> "#37474F"
            }
            animalCategory.background.setTint(
                android.graphics.Color.parseColor(badgeColor)
            )

            root.setOnClickListener { onAnimalClick(animal) }
        }
    }

    fun filter(query: String) {
        animals = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = animals.size
}
