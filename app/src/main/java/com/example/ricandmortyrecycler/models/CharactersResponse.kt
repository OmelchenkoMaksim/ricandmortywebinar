package com.example.ricandmortyrecycler.models

import com.example.ricandmortyrecycler.models.Character
import com.example.ricandmortyrecycler.models.Info

data class CharactersResponse(
    val info: Info,
    val results: List<Character>
)