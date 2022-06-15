package com.example.music.allSongs

import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.MyApplication
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
    // play music
    fun playMusic(id: Long){
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                val uri = Uri.parse(song.getData())
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(MyApplication.getContext(), uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }
}