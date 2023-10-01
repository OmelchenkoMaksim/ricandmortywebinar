package com.example.ricandmortyrecycler.network

import com.example.ricandmortyrecycler.models.CharactersResponse
import com.example.ricandmortyrecycler.models.LocationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {

    /*
    GsonConverterFactory определяет в какой объект упаковывать объект по возвращаемому значению в АПИ

В этом случае, GsonConverterFactory будет ожидать JSON-ответ от сервера,
который можно преобразовать в объект CharactersResponse.
Тип CharactersResponse определяет структуру, которая должна быть у JSON-ответа.
*/
    @GET("character")
    fun getCharacters(
        @Query("page") page: Int // для пагинации
    ): Call<CharactersResponse>


    @GET("location")
    fun getLocations(
        @Query("page") page: Int // для пагинации
    ): Call<LocationsResponse>
}
