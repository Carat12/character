package com.example.character.data.database

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.example.character.data.model.Character
import com.example.character.data.model.Episode

@Dao
interface DaoCache {
    @Insert(onConflict = REPLACE)
    fun insertCharacter(characters: List<Character>)

    @Query("update character set page = :page, cachedTime = :time where id between :minId and :maxId")
    fun updateCharacterPageWithTime(page: String, time: Long, minId: Int, maxId: Int)

    @Transaction
    fun insertCharacterByPage(page: String, characters: List<Character>, time: Long) {
        insertCharacter(characters)
        val minId = (page.toInt() - 1) * 20 + 1
        val maxId = page.toInt() * 20
        updateCharacterPageWithTime(page, time, minId, maxId)
    }

    @Query("select cachedTime from character where page = :page limit 1")
    fun characterByPageHasInserted(page: String): Long

    @Query("select * from character where page = :page")
    fun getCharacterByPage(page: String): List<Character>



    @Insert(onConflict = REPLACE)
    fun insertEpisode(episodes: List<Episode>)

    @Query("update episode set page = :page, cachedTime = :time where id between :minId and :maxId")
    fun updateEpisodePageAndTime(page: String, time: Long, minId: Int, maxId: Int)

    @Transaction
    fun insertEpisodeByPage(page: String, episodes: List<Episode>, time: Long){
        insertEpisode(episodes)
        val minId = (page.toInt() - 1) * 20 + 1
        val maxId = page.toInt() * 20
        updateEpisodePageAndTime(page, time, minId, maxId)
    }

    @Query("select cachedTime from episode where page = :page limit 1")
    fun episodeByPageHasInserted(page: String): Long

    @Query("select * from episode where page = :page")
    fun getEpisodeByPage(page: String): List<Episode>




    @Insert(onConflict = IGNORE)
    fun insertCharacterByEpisode(characters: List<Character>)

    @Query("select * from character where url in (:urlList)")
    fun getCharacterByEpisode(urlList: List<String>): List<Character>
}