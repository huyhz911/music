package com.example.music.database

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.music.MyApplication


class LocalMusicDataSoure {
  public  fun getSong(): ArrayList<SongInfo> {
        val songs: ArrayList<SongInfo> = ArrayList()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor =
                MyApplication.instance?.contentResolver?.query(uri,projection,null,null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                val position = 1
                while (!cursor.isAfterLast) {
                    val song = SongInfo(cursor.getLong(6),cursor.getString(0),cursor.getString(5),cursor.getString(1),cursor.getLong(4))
//                    song.setTitle(cursor.getString(0))
//                    song.setDuration(cursor.getLong(4))
//                    song.setArtist(cursor.getString(1))
//                    song.setAlbumName(cursor.getString(5))
//                    song.setAlbumId(cursor.getLong(6))
                    songs.add(song)
                    cursor.moveToNext()
                }
            }
        } catch (e: Exception) {
            Log.e("Media", e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
      return songs;
    }
}