package com.example.character.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.character.data.model.Character

data class CharacterByPage(
    @PrimaryKey
    val page: String,
    @Relation(parentColumn = "pageId", entityColumn = "characterPage")
    val characters: List<Character>
)

