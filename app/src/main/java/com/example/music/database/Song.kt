package com.example.music.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Parcelable
import com.example.music.MyApplication
import com.example.music.R
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
@Parcelize
class Song( var songID: Int?,
            var songName: String?,
            var albumName: String?,
            var artists: String?,
            var duration: Long?,
            var data: String?): Parcelable, Serializable {
    /**
     * Bkav HuyNgQe:lay anh bia ra de chuyen sang thong bao
     */
    fun getPicture(): Bitmap {
        var art: Bitmap = BitmapFactory.decodeResource(MyApplication.getContext().resources,
            R.drawable.bg_default_album_art)
        val uri = Uri.parse(data)
        val mmr = MediaMetadataRetriever()
        val bfo = BitmapFactory.Options()
        mmr.setDataSource(MyApplication.getContext(), uri)
        val rawArt: ByteArray? = mmr.embeddedPicture
        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)
        }
        return art
    }


}
