package com.example.character.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.Episode

@Database(entities = [Character::class, Episode::class], version = Config.DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseCache: RoomDatabase() {
    abstract fun getDao(): DaoCache

    companion object{
        fun getDatabase(context: Context): DatabaseCache{
            return Room.databaseBuilder(context, DatabaseCache::class.java, Config.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}