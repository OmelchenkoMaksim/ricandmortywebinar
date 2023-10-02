package com.example.ricandmortyrecycler.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ricandmortyrecycler.R
import com.example.ricandmortyrecycler.models.RickMortyItem

/**
 * ListAdapter — это специализированный подкласс RecyclerView.Adapter,
 * который упрощает работу с адаптерами, использующими DiffUtil для оптимизированного обновления данных.
 *
 * Инициализация адаптера: Когда вы создаете экземпляр ListAdapter,
 * вы передаете экземпляр DiffUtil.ItemCallback в качестве аргумента.
 */
class RickMortyAdapterDiffUtil(
    private val switchClickListener: OnSwitchClickListener
) : ListAdapter<RickMortyItem, RecyclerView.ViewHolder>(RickMortyDiffCallback()) {

    //    Обновление данных: Вместо прямого обновления списка в адаптере (как вы бы делали с обычным RecyclerView.Adapter),
//    вы используете метод submitList:
//    adapter.submitList(newList)
    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_CHARACTER = 1
        const val TYPE_DESCRIPTION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RickMortyItem.Title -> TYPE_TITLE
            is RickMortyItem.Character -> TYPE_CHARACTER
            is RickMortyItem.Description -> TYPE_DESCRIPTION
            else -> TODO()
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
        when (val item = getItem(position)) {
            is RickMortyItem.Title -> (holder as TitleViewHolder).bind(item)
            is RickMortyItem.Character -> (holder as CharacterViewHolder).bind(item)
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

        fun bind(character: RickMortyItem.Character) {
            nameTextView.text = character.name
            statusTextView.text = character.status
            speciesTextView.text = character.species
            Glide.with(itemView.context).load(character.image).into(imageView)
        }
    }


    /**
     * В основе DiffUtil лежит алгоритм, который вычисляет разницу между двумя списками и выдает
     * минимальный набор операций обновления для преобразования одного списка в другой.
     *
     * Это позволяет адаптеру автоматически обновлять только те элементы, которые были добавлены,
     * удалены или изменены, вместо перерисовки всего списка.
     * Это делает анимации и обновления более плавными и эффективными.
     *
     * DiffUtil.ItemCallback: Это абстрактный класс, который нужно реализовать,
     * чтобы DiffUtil мог понять, как сравнивать элементы списка.
     */
    class RickMortyDiffCallback : DiffUtil.ItemCallback<RickMortyItem>() {
        override fun areItemsTheSame(oldItem: RickMortyItem, newItem: RickMortyItem): Boolean {
            return oldItem == newItem // или другое условие для сравнения ID элементов
        }

        override fun areContentsTheSame(oldItem: RickMortyItem, newItem: RickMortyItem): Boolean {
            return oldItem == newItem // или другое условие для сравнения содержимого элементов
        }
    }

}
