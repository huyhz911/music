package com.example.music.playmedia

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.database.FavoriteSongs
import com.example.music.database.FavoriteSongsDatabaseDAO
import com.example.music.database.Song
import com.example.music.service.MediaPlaybackService
import kotlinx.coroutines.launch

/**
 * Created by Bkav HuyNgQe on 04/07/2022.
 */
class MediaPlaybackViewModel(
    val database: FavoriteSongsDatabaseDAO) : ViewModel() {
    /*HuyNgQe: live data thuoc tinh cua song*/
    private val _songName = MutableLiveData<String?>()
    val songName: MutableLiveData<String?>
        get() = _songName
    private val _songAuthor = MutableLiveData<String?>()
    val songAuthor: LiveData<String?>
        get() = _songAuthor
    private val _songPicture = MutableLiveData<Bitmap>()
    val songPicture: LiveData<Bitmap>
        get() = _songPicture
    /**
     * Bkav HuyNgQe:update ui song
     */
    fun updateUiSong(song: Song){
        _songName.value = song.songName
        _songAuthor.value= song.artists
        _songPicture.value = song.getPicture()
    }
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
            newFavoriteSongs.IS_FAVORITE = 2
            insert(newFavoriteSongs)
            viewModelScope.launch(){database.getAllSongs()}
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