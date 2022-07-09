package com.example.music.showallsongs

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.database.FavoriteSongs
import com.example.music.database.FavoriteSongsDatabaseDAO
import com.example.music.database.Song
import kotlinx.coroutines.launch

/**
 * Created by Bkav HuyNgQe on 07/07/2022.
 */
class AllSongsViewModel(
    val database: FavoriteSongsDatabaseDAO,
) : ViewModel() {
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
     fun insert(song: Song) {
        viewModelScope.launch {
            val newFavoriteSongs = FavoriteSongs()
            newFavoriteSongs.ID_PROVIDER = song.songID!!
            database.insert(newFavoriteSongs)
        }
    }
}