package com.example.music.database

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.music.MyApplication

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class LocalMusicDataSource {
    /**
     * Bkav HuyNgQe: lay bai hat
     */
    fun getSong(): ArrayList<Song> {
        val songs: ArrayList<Song> = ArrayList()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media._ID
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor: Cursor? = MyApplication.instance?.contentResolver?.query(uri,
            projection,
            selection,
            null,
            sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val song = Song(cursor.getInt(7),
                        cursor.getStringOrNull(0),
                        cursor.getStringOrNull(5),
                        cursor.getStringOrNull(1),
                        cursor.getLongOrNull(4),
                        cursor.getStringOrNull(2))
                    songs.add(song)
                    cursor.moveToNext()
                }
            }
            if (cursor != null) {
                cursor.close()
            }
      return songs;
    }
}