package com.karunada.vanya.ui.wiki

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karunada.vanya.R
import com.karunada.vanya.data.model.WikiItem
import com.karunada.vanya.databinding.ItemAnimalBinding

class WikiAdapter(
    private var items: List<WikiItem>,
    private val onItemClick: (WikiItem) -> Unit
) : RecyclerView.Adapter<WikiAdapter.WikiViewHolder>() {

    private var fullList: List<WikiItem> = items

    class WikiViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WikiViewHolder {
        val binding = ItemAnimalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WikiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WikiViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            animalName.text = item.name
            scientificName.text = item.scientificName
            animalCategory.text = item.category
            
            if (item.imageUrl.isNotEmpty()) {
                Glide.with(animalImage.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.app_logo)
                    .into(animalImage)
            } else if (item.imageRes != 0) {
                animalImage.setImageResource(item.imageRes)
            } else {
                animalImage.setImageResource(R.drawable.app_logo)
            }

            val badgeColor = when (item.category) {
                "Mammal" -> "#1565C0"
                "Bird" -> "#6A1B9A"
                "Reptile" -> "#BF360C"
                "Plant" -> "#2E7D32"
                else -> "#2E7D32"
            }
            animalCategory.background.setTint(Color.parseColor(badgeColor))

            root.setOnClickListener { onItemClick(item) }
        }
    }

    fun updateData(newItems: List<WikiItem>) {
        items = newItems
        fullList = newItems
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        items = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}