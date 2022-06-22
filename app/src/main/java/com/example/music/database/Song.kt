package com.example.music.database

import java.io.Serializable

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class Song( var songID: Int,
            var songName: String,
            var albumName: String,
            var artists: String,
            var duration: Long,
            var data: String) {
}