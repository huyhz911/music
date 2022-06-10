package com.example.music.mediaPlay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.SongInfo

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackViewModel(localMusicDataSource: LocalMusicDataSource): ViewModel() {

    var listSong = MutableLiveData<ArrayList<SongInfo>>()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
}