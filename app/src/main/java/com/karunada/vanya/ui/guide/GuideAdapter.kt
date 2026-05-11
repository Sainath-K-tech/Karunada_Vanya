package com.karunada.vanya.ui.guide

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karunada.vanya.data.model.GuideItem
import com.karunada.vanya.databinding.ItemGuideBinding

class GuideAdapter(
    private val guides: List<GuideItem>
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    inner class GuideViewHolder(val binding: ItemGuideBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = ItemGuideBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guides[position]
        with(holder.binding) {
            guideEmoji.text = guide.emoji
            guideAnimal.text = guide.animal
            guideSituation.text = guide.situation
            guideCardBackground.setBackgroundColor(Color.parseColor(guide.backgroundColor))

            // Format steps as numbered list
            guideSteps.text = guide.steps
                .mapIndexed { i, step -> "${i + 1}. $step" }
                .joinToString("\n")

            // Format do-nots as bullet list
            guideDoNot.text = guide.doNot
                .joinToString("\n") { "• $it" }
        }
    }

    override fun getItemCount() = guides.size
}