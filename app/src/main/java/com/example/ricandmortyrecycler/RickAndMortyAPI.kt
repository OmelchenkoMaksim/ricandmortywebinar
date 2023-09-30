package com.example.ricandmortyrecycler

import com.example.ricandmortyrecycler.models.CharactersResponse
import com.example.ricandmortyrecycler.models.LocationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character")
    fun getCharacters(
        @Query("page") page: Int // для пагинации
    ): Call<CharactersResponse>


    @GET("location")
    fun getLocations(
        @Query("page") page: Int // для пагинации
    ): Call<LocationsResponse>
}
