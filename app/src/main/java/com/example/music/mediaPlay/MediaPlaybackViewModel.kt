package com.example.music.mediaPlay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.Song

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackViewModel(localMusicDataSource: LocalMusicDataSource): ViewModel() {

    var listSong = MutableLiveData<ArrayList<Song>>()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
    // lay ten bai hat
    fun getSongName(id:Int):String{
        var songName: String ="not found"
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                songName =song.getTitle()
            }
        }
        return  songName
    }
}