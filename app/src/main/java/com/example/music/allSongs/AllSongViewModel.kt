package com.example.music.allSongs

import androidx.lifecycle.ViewModel
import com.example.music.database.LocalMusicDataSoure
import com.example.music.database.SongInfo

class AllSongViewModel(localMusicDataSoure: LocalMusicDataSoure) : ViewModel() {
    var listSong = listOf<SongInfo>()
    init {
        listSong = localMusicDataSoure.getSong()
    }
}