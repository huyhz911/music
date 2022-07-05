package com.example.music.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Bkav HuyNgQe on 16/06/2022.
 */
@Database(entities = [FavoriteSongs::class], version = 1, exportSchema = false)
abstract class FavoriteSongsDatabase: RoomDatabase() {
      abstract val favoriteSongsDatabaseDAO: FavoriteSongsDatabaseDAO
      companion object{
            @Volatile
            private var INSTANCE: FavoriteSongsDatabase? = null
            fun getInstance(context: Context): FavoriteSongsDatabase {
                  synchronized(this){
                        var instance = INSTANCE
                        if (instance == null){
                              instance = Room.databaseBuilder(
                                    context.applicationContext,
                                    FavoriteSongsDatabase::class.java,
                                    "favorite_songs_database")
                                    .fallbackToDestructiveMigration()
                                    .allowMainThreadQueries()
                                    .build()
                              INSTANCE = instance
                        }
                        return instance
                  }
            }
      }
}