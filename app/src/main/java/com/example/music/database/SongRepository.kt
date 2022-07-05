package com.example.music.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.music.MyApplication
import com.example.music.R

/**
 * Created by Bkav HuyNgQe on 20/06/2022.
 */
class SongRepository() {
    private val localMusicDataSource = LocalMusicDataSource()
    /**
     * Bkav HuyNgQe:lay du lieu tu may
     */
    fun getSongs(): ArrayList<Song> {
        return localMusicDataSource.getSong()
    }


}