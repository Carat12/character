package com.example.character.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.CharacterResponse
import com.example.character.data.model.Episode
import com.example.character.data.model.EpisodeResponse
import com.example.character.data.network.MyApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRepository {

    private val api = MyApi()

    //character
    private var hasNextPageCharacter: Boolean = true
    private var getCharacterErrorMsg: String? = null
    private var maxPage: Int? = null

    fun getCharacter(page: String, result: MutableLiveData<ArrayList<Character>>) {
        Log.d("woozi","request")
        api.getCharacter(page).enqueue(object : Callback<CharacterResponse>{
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("woozi","success")
                    hasNextPageCharacter = response.body()!!.info.next != null
                    maxPage = response.body()!!.info.pages
                    result.value = response.body()!!.results
                }
                else{
                    Log.d("woozi","error")
                    getCharacterErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.ERROR_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.d("woozi","fail")
                getCharacterErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getGetCharacterErrorMsg(): String{
        return getCharacterErrorMsg!!
    }

    fun hasNextPageCharacter(): Boolean{
        return hasNextPageCharacter
    }

    //episode
    private var hasNextPageEpisode: Boolean = true
    private var getEpisodeErrorMsg: String? = null

    fun getEpisode(page: String, result: MutableLiveData<ArrayList<Episode>>) {
        api.getEpisode(page).enqueue(object : Callback<EpisodeResponse>{
            override fun onResponse(
                call: Call<EpisodeResponse>,
                response: Response<EpisodeResponse>
            ) {
                if(response.isSuccessful){
                    hasNextPageEpisode = response.body()!!.info.next != null
                    maxPage = response.body()!!.info.pages
                    result.value = response.body()!!.results
                }else{
                    getEpisodeErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.ERROR_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
                getEpisodeErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getGetEpisodeErrorMsg(): String{
        return getEpisodeErrorMsg!!
    }

    fun hasNextPageEpisode(): Boolean{
        return hasNextPageEpisode
    }

    //single character
    private var getSingleCharacterErrorMsg: String? = null

    fun getSingleCharacter(endpoint: String, result: MutableLiveData<Character>) {
        api.getSingleCharacter(endpoint).enqueue(object : Callback<Character>{
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if(response.isSuccessful)
                    result.value = response.body()!!
                else{
                    getSingleCharacterErrorMsg = "$endpoint: " + JSONObject(response.errorBody()!!.string()).getString(Config.ERROR_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                getSingleCharacterErrorMsg = "$endpoint: " + t.message.toString()
                result.value = null
            }
        })
    }

    fun getGetSingleCharacterErrorMsg(): String{
        return getSingleCharacterErrorMsg!!
    }

    fun getMaxPage(): Int? {
        return maxPage
    }
}