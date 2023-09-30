package com.example.ricandmortyrecycler.models

sealed class RickMortyItem {
    data class Title(val text: String) : RickMortyItem()
    data class CharacterInfo(val character: Character) : RickMortyItem()
    data class Description(val text: String) : RickMortyItem()
}
