package com.example.character.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.Episode
import com.example.character.data.repository.CacheRepository
import com.example.character.data.repository.MyRepository

class MyViewModel(application: Application) : ViewModel() {
    //live data
    var characterList: MutableLiveData<ArrayList<Character>> = MutableLiveData()
    var singleCharacter: MutableLiveData<Character> = MutableLiveData()
    var episodeList: MutableLiveData<ArrayList<Episode>> = MutableLiveData()
    var currentPage: MutableLiveData<String> = MutableLiveData()

    //cached live data
    var cachedCharacterList: MutableLiveData<ArrayList<Character>> = MutableLiveData()
    var cachedEpisodeList: MutableLiveData<ArrayList<Episode>> = MutableLiveData()

    //repository
    private val repository = MyRepository()
    private val cacheRepository = CacheRepository.getInstance(application)

    init {
        currentPage.value = "1"
    }

    //character
    fun getCharacter(hasNetwork: Boolean) {
        if (hasNetwork) {
            Log.d("woozi", "cha: get from api")
            repository.getCharacter(currentPage.value!!, characterList)
        } else
            getCachedCharacterByPage()
    }

    fun getCachedCharacterByPage() {
        Log.d("woozi", "cha: get from cache")
        cacheRepository.getCharacterByPage(currentPage.value!!, cachedCharacterList)
    }

    fun cacheCharacterByPage() {
        Log.d("woozi", "cache char p: ${currentPage.value}")
        cacheRepository.cacheCharacterByPage(currentPage.value!!, characterList.value!!.toList())
    }

    fun getGetCharacterErrorMsg(): String {
        return repository.getGetCharacterErrorMsg()
    }

    fun hasNextPageCharacter(): Boolean {
        return repository.hasNextPageCharacter()
    }

    //episode
    fun getEpisode(hasNetwork: Boolean) {
        if(hasNetwork)
            repository.getEpisode(currentPage.value!!, episodeList)
        else
            getCachedEpisodeByPage()
    }

    fun getCachedEpisodeByPage(){
        Log.d("woozi", "episode: get from cache")
        cacheRepository.getEpisodeByPage(currentPage.value!!, cachedEpisodeList)
    }

    fun cacheEpisodeByPage(){
        Log.d("woozi", "cache episode p: ${currentPage.value}")
        cacheRepository.cacheEpisodeByPage(currentPage.value!!, episodeList.value!!.toList())
    }

    fun getGetEpisodeErrorMsg(): String {
        return repository.getGetEpisodeErrorMsg()
    }

    fun hasNextPageEpisode(): Boolean {
        return repository.hasNextPageEpisode()
    }

    //get single character
    fun getSingleCharacter(url: String) {
        val endpoint = url.substring(Config.BASE_URL.length)
        repository.getSingleCharacter(endpoint, singleCharacter)
    }

    fun getCachedCharacterByEpisode(episode: Episode) {
        val urlList = episode.characters
        cacheRepository.getCharacterByEpisode(urlList, characterList)
    }

    fun cacheCharacterByEpisode(characterList: ArrayList<Character>) {
        cacheRepository.cacheCharacterByEpisode(characterList)
    }

    fun getGetSingleCharacterErrorMsg(): String {
        return repository.getGetSingleCharacterErrorMsg()
    }

    /*fun checkEnteredPage(text: CharSequence?): Boolean {
        val maxPage = repository.getMaxPage()?: Int.MAX_VALUE
        val page = text.toString().toInt()
        if(page in 0..maxPage) {
            currentPage = text.toString()
            return true
        }
        return false
    }*/
    //change page
    fun nextPage() {
        currentPage.value = (currentPage.value!!.toInt() + 1).toString()
    }

    fun prevPage() {
        currentPage.value = (currentPage.value!!.toInt() - 1).toString()
    }
}