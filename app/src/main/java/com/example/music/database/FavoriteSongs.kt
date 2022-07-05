package com.example.music.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Bkav HuyNgQe on 16/06/2022.
 */
/**
 * Bkav HuyNgQe: tao bang
 */
@Entity(tableName = "favorite_songs_provider")
data class FavoriteSongs(
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0,
    @ColumnInfo(name = "song_ID")
    var ID_PROVIDER: Int= 0,
    @ColumnInfo(name = "song_rating")
    var IS_FAVORITE: Int = 0,
    @ColumnInfo(name = "count_of_play")
    var COUNT_OF_PLAY: Int = 0
)

