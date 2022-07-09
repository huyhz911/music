package com.example.music.playmedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.database.FavoriteSongsDatabaseDAO

/**
 * Created by Bkav HuyNgQe on 04/07/2022.
 */
class MediaPlaybackViewModelFactory(
    val database: FavoriteSongsDatabaseDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaPlaybackViewModel::class.java)){
            return MediaPlaybackViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}