package com.example.character.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun toString(urls: List<String>): String{
        return gson.toJson(urls)
    }

    @TypeConverter
    fun toUrls(string: String): List<String>{
        if(string.isNullOrBlank())
            return ArrayList()
        val type = object : TypeToken<List<String>>(){}.type
        return gson.fromJson(string, type)
    }
}