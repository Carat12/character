package com.example.character.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.character.data.database.Converters
import java.io.Serializable

@Entity
data class Episode(
    val air_date: String,
    @TypeConverters(Converters::class)
    val characters: List<String>,
    val episode: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val page: String? = null,
    val cachedTime: Long? = null
    //val created: String,
    //val url: String
): Serializable