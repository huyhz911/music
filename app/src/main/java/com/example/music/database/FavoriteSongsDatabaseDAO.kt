package com.example.music.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by Bkav HuyNgQe on 16/06/2022.
 */
@Dao
interface FavoriteSongsDatabaseDAO {
    //them bai hat ua thich
    @Insert
    suspend fun insert(song: FavoriteSongs)
    // lay ra tat ca bai hat ua thich
    @Query("SELECT * FROM favorite_songs_provider ORDER BY ID DESC")
    fun getAllSongs(): LiveData<List<FavoriteSongs>>
}