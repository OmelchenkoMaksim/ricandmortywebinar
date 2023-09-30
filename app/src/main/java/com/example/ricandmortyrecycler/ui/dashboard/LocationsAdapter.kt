package com.example.ricandmortyrecycler.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ricandmortyrecycler.R
import com.example.ricandmortyrecycler.models.Location

class LocationsAdapter(private val locations: List<Location>) : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.locationNameTextView)
        // другие поля, которые вы хотите отображать
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.nameTextView.text = location.name
        // Здесь можно установить другие поля
    }
}