package com.example.music.mediaPlayService

import android.app.Notification
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.MyApplication.Companion.CHANNEL_ID
import com.example.music.R
import com.example.music.database.Song
import com.example.music.database.SongRepository


/**
 * Created by Bkav HuyNgQe on 18/06/2022.
 */
class MediaPlaybackService(): Service() {

    var songRepository = SongRepository()
    var listSong = MutableLiveData<ArrayList<Song>>()
    var index: Int = -1
    var mediaPlayer: MediaPlayer

    companion object{
        private const val SONG_UPDATE_UI ="send song"
        private const val DATA ="data"
    }
    init {
        listSong.value = songRepository.getSongs()
        mediaPlayer = MediaPlayer()
    }

    /**
     * Bkav HuyNgQe: play music
     */
    fun playMusic(song: Song){
                    mediaPlayer.reset()
                    val uri = Uri.parse(song.data)
                    mediaPlayer.setDataSource(MyApplication.getContext(), uri)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    index = listSong.value!!.indexOf(song)
                    mediaPlayer.setOnCompletionListener{
                        if (index < listSong.value!!.size){
                        nextSongAuto(index +1 )
                            // send song
                            val intent = Intent(SONG_UPDATE_UI)
                            intent.putExtra(DATA,(index+1).toString())
                            sendBroadcast(intent)
                    }else{
                        mediaPlayer.stop()
                    } }
            }

 /**
  * Bkav HuyNgQe: auto next song
  */
    fun nextSongAuto(index:Int){
         playMusic(listSong.value!!.get(index))
    }
    /**
     * Bkav HuyNgQe: next song do bam nut next
     */
    fun nextSong(song: Song?){
        if (song != null) {
            nextSongAuto(index +1)
        }
    }
    /**
     * Bkav HuyNgQe: pause music
     */
    fun pauseMusic(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }
    /**
     * Bkav HuyNgQe: resumeMusic
     */
    fun resumeMusic(){
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
    }
    /**
     * Bkav HuyNgQe: check isPlaying
     */
    fun checkIsPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
    /**
     * Bkav HuyNgQe:Gui thong tin len thong bao
     */
    fun sendNotification(song: Song) {
        val intent = Intent(MyApplication.getContext(), ActivityMusic::class.java)
        val pendingIntent = getActivity(this, 0, intent, FLAG_UPDATE_CURRENT)
        val remoteViews= RemoteViews(packageName, R.layout.song_notification)
        remoteViews.setImageViewBitmap(R.id.imageAlbumNotification,song.getPicture())
        remoteViews.setImageViewResource(R.id.back_notification, R.drawable.ic_rew_dark)
        remoteViews.setImageViewResource(R.id.background_play_pause_button, R.drawable.ic_fab_play_btn_normal)
        remoteViews.setImageViewResource(R.id.next_notification, R.drawable.ic_fwd_dark)
        remoteViews.setImageViewResource(R.id.back_notification, R.drawable.ic_rew_dark)
        remoteViews.setImageViewResource(R.id.button_play, R.drawable.ic_media_play_dark)
        remoteViews.setImageViewResource(R.id.button_pause, R.drawable.ic_media_pause_dark)

        val notification:Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
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
