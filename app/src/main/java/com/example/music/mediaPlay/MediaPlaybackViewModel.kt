package com.example.music.mediaPlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.database.FavoriteSongs
import com.example.music.database.FavoriteSongsDatabaseDAO
import com.example.music.database.Song
import com.example.music.mediaPlayService.MediaPlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.IDN

/**
 * Created by Bkav HuyNgQe on 04/07/2022.
 */
class MediaPlaybackViewModel(
    val database: FavoriteSongsDatabaseDAO) : ViewModel() {
    /**
     * Bkav HuyNgQe:insert bai hat vao room
     */
    private suspend fun insert(song : FavoriteSongs){
        database.insert(song)
    }
    private fun remove(id: Int){
        database.removeFavoriteSong(id)
    }
    /**
     * Bkav HuyNgQe: khi click vao nut like bai hat se duoc inset vao favoriteSong
     */
    fun onClickLike() {
        viewModelScope.launch {
            val newFavoriteSongs = FavoriteSongs()
            newFavoriteSongs.ID_PROVIDER = MediaPlaybackService.index
            insert(newFavoriteSongs)
            viewModelScope.launch(){
                database.getAllSongs()
            }
        }
    }
    /**
     * Bkav HuyNgQe: khi bam vao dislike xoa baif hat khoi room database
     */
    fun onClickDislike() {
        viewModelScope.launch {
            remove(MediaPlaybackService.index)
        }
    }



}