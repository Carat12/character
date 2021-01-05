package com.example.character.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.character.app.Config
import com.example.character.data.database.DatabaseCache
import com.example.character.data.model.Character
import com.example.character.data.model.Episode
import kotlin.Exception

class CacheRepository(context: Context) {

    companion object{
        private var instance: CacheRepository? = null

        fun getInstance(context: Context): CacheRepository{
            if(instance == null)
                instance = CacheRepository(context)
            return instance!!
        }
    }

    private val db = DatabaseCache.getDatabase(context)
    private val daoCache = db.getDao()

    fun cacheCharacterByPage(page: String, characters: List<Character>) {
        val time = System.currentTimeMillis()
        var lastCachedTime: Long?
        try {
            lastCachedTime = daoCache.characterByPageHasInserted(page)
        }catch (e: Exception){
            lastCachedTime = null
        }
        Log.d("woozi", "last: $lastCachedTime, now: $time")
        if(lastCachedTime == null || (time - lastCachedTime) > Config.CACHE_INTERVAL)
            daoCache.insertCharacterByPage(page, characters, time)
    }

    fun getCharacterByPage(page: String, result: MutableLiveData<ArrayList<Character>>){
        val characterList: List<Character>? = try {
            daoCache.getCharacterByPage(page)
        }catch (e: Exception){
            Log.d("woozi", e.message.toString())
            null
        }
        Log.d("woozi", "${characterList?.size}")
        if(characterList != null)
            result.value = ArrayList(characterList)
        else
            result.value = ArrayList()
    }

    fun cacheEpisodeByPage(page: String, episodes: List<Episode>){
        val time = System.currentTimeMillis()
        var lastCachedTime: Long?
        try{
            lastCachedTime = daoCache.episodeByPageHasInserted(page)
        }catch (e: Exception){
            lastCachedTime = null
        }
        Log.d("woozi", "episode last: $lastCachedTime, now: $time")
        if(lastCachedTime == null || (time - lastCachedTime) > Config.CACHE_INTERVAL)
            daoCache.insertEpisodeByPage(page, episodes, time)
    }

    fun getEpisodeByPage(page: String, result: MutableLiveData<ArrayList<Episode>>){
        val episodeList: List<Episode>? = try{
            daoCache.getEpisodeByPage(page)
        }catch (e: Exception){
            null
        }
        Log.d("woozi", "epi size: ${episodeList?.size}")
        if(episodeList != null)
            result.value = ArrayList(episodeList)
        else
            result.value = ArrayList()
    }

    fun cacheCharacterByEpisode(characters: List<Character>){
        daoCache.insertCharacterByEpisode(characters)
    }

    fun getCharacterByEpisode(urlList: List<String>, result : MutableLiveData<ArrayList<Character>>) {
        val characterList: List<Character>? = try {
            daoCache.getCharacterByEpisode(urlList)
        }catch (e: Exception){
            null
        }
        Log.d("woozi", "char by epi size: ${characterList?.size}")
        if(characterList != null)
            result.value = ArrayList(characterList)
        else
            result.value = ArrayList()
    }
}