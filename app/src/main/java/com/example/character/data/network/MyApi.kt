package com.example.character.data.network

import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.CharacterResponse
import com.example.character.data.model.EpisodeResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {

    @GET(EndPoint.CHARACTER_ENDPOINT)
    fun getCharacter(@Query("page") page: String): Call<CharacterResponse>

    @GET(EndPoint.EPISODE_ENDPOINT)
    fun getEpisode(@Query("page") page: String): Call<EpisodeResponse>

    @GET("{endpoint}")
    fun getSingleCharacter(@Path("endpoint") endpoint: String): Call<Character>

    companion object {
        operator fun invoke(): MyApi {
            return Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }
    }
}