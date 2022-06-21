package com.example.music.mediaPlayService

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.example.music.MyApplication
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.Song
import com.example.music.database.SongRepository


/**
 * Created by Bkav HuyNgQe on 18/06/2022.
 */
class MediaPlaybackService(): Service() {
    var songRepository = SongRepository()
    var listSong = songRepository.listSong
    private var mediaPlayer: MediaPlayer? = null
    init {
        listSong.value = LocalMusicDataSource().getSong()

    }

    /**
     * Bkav HuyNgQe: play music
     */
    fun playMusic(id: Int){
        listSong.value?.forEach { song ->
            if (song.getAlbumId()==id){
                if (mediaPlayer == null){
                    mediaPlayer = MediaPlayer()
                    val uri = Uri.parse(song.getData())
                    mediaPlayer?.setDataSource(MyApplication.getContext(), uri)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                }else{
                    mediaPlayer!!.stop()
                    mediaPlayer = null
                    mediaPlayer = MediaPlayer()
                    val uri = Uri.parse(song.getData())
                    mediaPlayer?.setDataSource(MyApplication.getContext(), uri)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                }

            }
        }
    }
    /**
     * Bkav HuyNgQe: pause music
     */
    fun pauseMusic(){
        mediaPlayer?.pause()
    }

    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Binder given to clients
    private val binder = LocalBinder()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MediaPlaybackService = this@MediaPlaybackService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}
