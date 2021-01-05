package com.example.character.data.model

data class CharacterResponse(
    val info: Info,
    val results: ArrayList<Character>
)

data class EpisodeResponse(
    val info: Info,
    val results: ArrayList<Episode>
)

data class Info(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String?
)