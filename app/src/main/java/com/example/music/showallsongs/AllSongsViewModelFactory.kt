package com.example.music.showallsongs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.database.FavoriteSongsDatabaseDAO

/**
 * Created by Bkav HuyNgQe on 07/07/2022.
 */
class AllSongsViewModelFactory(
    val database: FavoriteSongsDatabaseDAO,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllSongsViewModel::class.java)) {
            return AllSongsViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}