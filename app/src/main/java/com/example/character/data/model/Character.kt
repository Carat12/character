package com.example.character.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Character(
    @PrimaryKey
    val id: Int,
    val image: String,
    @Embedded(prefix = "loc_")
    val location: Location,
    val name: String,
    @Embedded(prefix = "origin_")
    val origin: Location,
    val species: String,
    val status: String,
    val url: String,
    val page: String? = null,
    val cachedTime: Long? = null
    //val created: String,
    //val episode: ArrayList<String>,
    //val gender: String,
    //val type: String,
) : Serializable