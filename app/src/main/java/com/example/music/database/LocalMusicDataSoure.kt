package com.example.music.database

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor =
                MyApplication.instance?.contentResolver?.query(uri,projection,null,null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                val position = 1
                while (!cursor.isAfterLast) {
                    val song = Song(cursor.getInt(7),
                        cursor.getString(0),
                        cursor.getString(5),
                        cursor.getString(1),
                        cursor.getLong(4),
                        cursor.getString(2))
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