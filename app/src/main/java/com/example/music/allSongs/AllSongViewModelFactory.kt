package com.example.music.allSongs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.database.LocalMusicDataSource
/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class AllSongViewModelFactory (
    private val dataSource: LocalMusicDataSource
): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllSongViewModel::class.java)) {
            return AllSongViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}