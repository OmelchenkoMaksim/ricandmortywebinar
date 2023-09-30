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

class CharactersAdapter(private val characters: List<Character>)
    : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val speciesTextView: TextView = itemView.findViewById(R.id.speciesTextView)
        val imageView: ImageView = itemView.findViewById(R.id.characterImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.name
        holder.statusTextView.text = character.status
        holder.speciesTextView.text = character.species
        Glide.with(holder.imageView.context).load(character.image).into(holder.imageView)
    }

    override fun getItemCount() = characters.size
}