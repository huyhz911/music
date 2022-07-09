package com.example.music.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Bkav HuyNgQe on 16/06/2022.
 */
@Dao
interface FavoriteSongsDatabaseDAO {
    //update bai hat
    @Update
    suspend fun update(song: FavoriteSongs)
    //them bai hat ua thich
    @Insert
    suspend fun insert(song: FavoriteSongs)
    // lay ra tat ca bai hat ua thich
    @Query("SELECT * FROM favorite_songs_provider")
    fun getAllSongs(): List<FavoriteSongs>
    @Query("DELETE FROM favorite_songs_provider WHERE song_ID = :key ")
    fun removeFavoriteSong(key:Int)


}