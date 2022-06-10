package com.example.music.mediaPlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.database.LocalMusicDataSource

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackViewModelFactory(
    private val dataSource: LocalMusicDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaPlaybackViewModel::class.java)){
            return MediaPlaybackViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}