package com.example.music.mediaPlayService

import android.app.*
import android.app.PendingIntent.*
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.MyApplication.Companion.CHANNEL_ID
import com.example.music.R
import com.example.music.allSongs.AllSongsFragment
import com.example.music.allSongs.GetSongPicture
import com.example.music.database.LocalMusicDataSource
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
            if (song.songID==id){
                if (mediaPlayer == null){
                    mediaPlayer = MediaPlayer()
                    val uri = Uri.parse(song.data)
                    mediaPlayer?.setDataSource(MyApplication.getContext(), uri)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                }else{
                    mediaPlayer!!.stop()
                    mediaPlayer = null
                    mediaPlayer = MediaPlayer()
                    val uri = Uri.parse(song.data)
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
        val bundle: Bundle? = intent?.extras
        if (bundle!= null){
            val picture: GetSongPicture = bundle.get("pictureSong") as GetSongPicture
            sendNotification(picture)
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(picture: GetSongPicture) {
        val intent = Intent(MyApplication.getContext(), ActivityMusic::class.java)
        val pendingIntent = getActivity(this, 0, intent, FLAG_UPDATE_CURRENT)
        val remoteViews= RemoteViews(packageName, R.layout.song_notification)
        remoteViews.setImageViewBitmap(R.id.imageAlbumNotification,picture.getPicture())
        remoteViews.setImageViewResource(R.id.back_notification, R.drawable.ic_rew_dark)
        remoteViews.setImageViewResource(R.id.background_play_pause_button, R.drawable.ic_fab_play_btn_normal)
        remoteViews.setImageViewResource(R.id.next_notification, R.drawable.ic_fwd_dark)
        remoteViews.setImageViewResource(R.id.back_notification, R.drawable.ic_rew_dark)
        remoteViews.setImageViewResource(R.id.button_play, R.drawable.ic_media_play_dark)
        remoteViews.setImageViewResource(R.id.button_pause, R.drawable.ic_media_pause_dark)

        val notification:Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
            .setSound(null)
            .build()
        startForeground(1, notification)
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
