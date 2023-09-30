package com.example.ricandmortyrecycler.models

import com.example.ricandmortyrecycler.models.Info
import com.example.ricandmortyrecycler.models.Location

data class LocationsResponse(
    val info: Info,
    val results: List<Location>
)