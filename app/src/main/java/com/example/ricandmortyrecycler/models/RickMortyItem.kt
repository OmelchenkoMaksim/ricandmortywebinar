package com.example.ricandmortyrecycler.models

sealed class RickMortyItem {
    data class Title(val text: String) : RickMortyItem()
    data class Description(val text: String) : RickMortyItem()

    data class Character(
        val name: String,
        val status: String,
        val species: String,
        val image: String
    ) : RickMortyItem()

}
