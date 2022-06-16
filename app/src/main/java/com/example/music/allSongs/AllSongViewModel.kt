package com.example.music.allSongs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.Song


/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class AllSongViewModel(localMusicDataSource: LocalMusicDataSource) : ViewModel() {
   var listSong = MutableLiveData<ArrayList<Song>>()
    private val mediaPlayer = MediaPlayer()
    init {
        listSong.value = localMusicDataSource.getSong()

    }
    // play music
    fun playMusic(id: Long){
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                val uri = Uri.parse(song.getData())
                mediaPlayer.setDataSource(MyApplication.getContext(), uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }
    // pause music
    fun pauseMusic(){
        mediaPlayer.pause()
    }
    // lay anh bia album
    fun getCoverPicture(id: Long): Bitmap{
        var art: Bitmap = BitmapFactory.decodeResource(MyApplication.getContext().resources , R.drawable.bg_default_album_art)
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                val uri = Uri.parse(song.getData())
                val mmr = MediaMetadataRetriever()
                val bfo = BitmapFactory.Options()
                mmr.setDataSource(MyApplication.getContext(), uri)
                val rawArt: ByteArray? = mmr.embeddedPicture
                if (null != rawArt){ art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)}
            }
        }
        return  art
    }
    // lay ten bai hat
    fun getSongName(id:Long):String{
        var songName: String ="Khong xac dinh"
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                songName =song.getTitle()
            }
        }
        return  songName
    }
    //lay ten tac gia
    fun getSongAuthor(id:Long):String{
        var songAuthor: String ="Khong xac dinh"
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                songAuthor =song.getArtist()
            }
        }
        return  songAuthor
    }


}