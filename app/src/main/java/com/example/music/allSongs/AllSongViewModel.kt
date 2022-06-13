package com.example.music.allSongs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.Song
/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class AllSongViewModel(localMusicDataSource: LocalMusicDataSource) : ViewModel() {
   var listSong = MutableLiveData<ArrayList<Song>>()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
    fun getAlbumArt(){

    }
}