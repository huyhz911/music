package com.example.music.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
@Parcelize
class Song( var songID: Int,
            var songName: String,
            var albumName: String,
            var artists: String,
            var duration: Long,
            var data: String): Parcelable, Serializable {
}