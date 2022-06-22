package com.example.music.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.music.MyApplication
import com.example.music.R

/**
 * Created by Bkav HuyNgQe on 20/06/2022.
 */
class SongRepository() {
    private val localMusicDataSource = LocalMusicDataSource()
    var listSong = MutableLiveData<ArrayList<Song>>()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
    /**
     * Bkav HuyNgQe: lay anh bia album
     */
    fun getCoverPicture(id: Int):Bitmap{
        var art: Bitmap = BitmapFactory.decodeResource(MyApplication.getContext().resources , R.drawable.bg_default_album_art)
        listSong.value?.forEach { song ->
            if (song.songID==id){
                val uri = Uri.parse(song.data)
                val mmr = MediaMetadataRetriever()
                val bfo = BitmapFactory.Options()
                mmr.setDataSource(MyApplication.getContext(), uri)
                val rawArt: ByteArray? = mmr.embeddedPicture
                if (null != rawArt){ art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)}
            }
        }
        return  art
    }
    /**
     * Bkav HuyNgQe: lay ten bai hat
     */
    fun getSongName(id:Int):String{
        var songName: String =MyApplication.getContext().getString(R.string.not_found)
        listSong.value?.forEach { song ->
            if (song.songID==id){
                songName =song.songName
            }
        }
        return  songName
    }
    /**
     * Bkav HuyNgQe:lay ten tac gia
     */
    fun getSongAuthor(id:Int):String{
        var songAuthor: String =MyApplication.getContext().getString(R.string.not_found)
        listSong.value?.forEach { song ->
            if (song.songID==id){
                songAuthor = song.artists
            }
        }
        return  songAuthor
    }
}