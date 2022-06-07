package com.example.music.allSongs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.SongInfo

class AllSongViewModel(localMusicDataSource: LocalMusicDataSource) : ViewModel() {
   var listSong = MutableLiveData<ArrayList<SongInfo>>()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
    fun getAlbumArt(){

    }
}