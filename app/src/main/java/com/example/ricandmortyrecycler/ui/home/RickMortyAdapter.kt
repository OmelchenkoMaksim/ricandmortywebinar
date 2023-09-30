package com.example.ricandmortyrecycler.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ricandmortyrecycler.R
import com.example.ricandmortyrecycler.models.Character
import com.example.ricandmortyrecycler.models.RickMortyItem

// Интерфейс для того что бы сделать передачу логики нажатия на свитчер и реализовать ее во фрагменте
interface OnSwitchClickListener {
    fun onSwitchClicked()
}

// Адаптер для нескольких вью тайпов
class RickMortyAdapter(
    private val items: List<RickMortyItem>,
    private val switchClickListener: OnSwitchClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_CHARACTER = 1
        const val TYPE_DESCRIPTION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RickMortyItem.Title -> TYPE_TITLE
            is RickMortyItem.CharacterInfo -> TYPE_CHARACTER
            is RickMortyItem.Description -> TYPE_DESCRIPTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_title, parent, false)
                TitleViewHolder(view)
            }

            TYPE_CHARACTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_character, parent, false)
                CharacterViewHolder(view)
            }

            TYPE_DESCRIPTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_description, parent, false)
                DescriptionViewHolder(view)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RickMortyItem.Title -> (holder as TitleViewHolder).bind(item)
            is RickMortyItem.CharacterInfo -> (holder as CharacterViewHolder).bind(item.character)
            is RickMortyItem.Description -> (holder as DescriptionViewHolder).bind(item)
        }
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        fun bind(item: RickMortyItem.Title) {
            titleTextView.text = item.text
        }
    }

    inner class DescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val switch: View = itemView.findViewById(R.id.switchMeUp)

        fun bind(item: RickMortyItem.Description) {
            descriptionTextView.text = item.text

            switch.setOnClickListener {
                switchClickListener.onSwitchClicked()
            }
        }
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        private val speciesTextView: TextView = itemView.findViewById(R.id.speciesTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.characterImageView)

        fun bind(character: Character) {
            nameTextView.text = character.name
            statusTextView.text = character.status
            speciesTextView.text = character.species
            Glide.with(itemView.context).load(character.image).into(imageView)
        }
    }


    override fun getItemCount() = items.size
}
