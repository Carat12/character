package com.example.character.app

object Config {

    const val TITLE = "Rick and Morty"

    const val BASE_URL = "https://rickandmortyapi.com/api/"

    const val ERROR_KEY = "error"
    const val CONTENT_KEY = "content"
    const val CONTENT_CHARACTER = "Character"
    const val CONTENT_EPISODE = "Episode"
    val CONTENT_TITLES = arrayListOf(CONTENT_CHARACTER, CONTENT_EPISODE)

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "mydb"

    const val CACHE_INTERVAL = 10*60*1000

    const val NETWORK_OFFLINE_MESSAGE = "Network offline!"
}