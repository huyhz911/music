package com.example.music.service

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
class MediaPlaybackService(): Service(), MediaPlayer.OnCompletionListener{

    val intent = Intent(SONG_UPDATE_UI)
    var songRepository = SongRepository()
    var listSong = MutableLiveData<ArrayList<Song>>()
    var mediaPlayer: MediaPlayer
    var checkShuffle :Boolean = false
    var checkRepeatOff : Boolean = true
    var checkRepeatOne : Boolean = false
    var checkLike : Boolean = false
    lateinit var totalDuration: String
    lateinit var currentTime: String

    companion object{
        // de tuong tc giua 2 viewModel
        var index: Int = -1
        private const val SONG_UPDATE_UI ="song update ui"
        private const val DATA ="data"
        private const val DATA_REPEAT ="data repeat"
        private const val DATA_SHUFFLE ="data shuffle"
    }
    init {
        listSong.value = songRepository.getSongs()
        mediaPlayer = MediaPlayer()
    }
/**
 * Bkav HuyNgQe:kiem tra chế độ nghe nhạc
 */
    override fun onCompletion(mp: MediaPlayer?) {
        if (checkShuffle) {
            playRandom()
        } else {
            if (!checkRepeatOff) {
                repeatOn()
            } else {
                if (checkRepeatOne) {
                    repeatOne()
                } else {
                    if (checkRepeatOff) {
                        if ((index + 1) == listSong.value?.size) {
                            mediaPlayer.stop()
                        } else {
                            nextSongAuto(index + 1)
                        }
                    }
                }
            }
        }
    }
    /**
     * Bkav HuyNgQe:get current time
     */
    fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
/**
 * Bkav HuyNgQe: lap lai 1 bai hat
 */
fun repeatOne() {
     checkRepeatOne = true
    mediaPlayer.setOnCompletionListener {nextSongAuto(index) }
}

    /**
     * Bkav HuyNgQe: cho nhac play het list lai quay lai tu dau lit
     */
    fun repeatOn() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.setOnCompletionListener {
                checkRepeatOff = false
                if ((index + 1) == listSong.value?.size) {
                    intent.putExtra(DATA_REPEAT, (0).toString())
                    sendBroadcast(intent)
                    nextSongAuto(0)
                } else {
                    nextSongAuto(index + 1)
                }
            }
        } else {
            checkRepeatOff = false
            if ((index + 1) == listSong.value?.size) {
                intent.putExtra(DATA_REPEAT, (0).toString())
                sendBroadcast(intent)
                nextSongAuto(0)
            } else {
                nextSongAuto(index + 1)
            }
        }
    }

    /**
     * Bkav HuyNgQe: play music
     */
    fun playMusic(song: Song) {
        sendNotification(song)
        mediaPlayer.reset()
        val uri = Uri.parse(song.data)
        mediaPlayer.setDataSource(MyApplication.getContext(), uri)
        mediaPlayer.prepare()
        mediaPlayer.start()
        index = listSong.value!!.indexOf(song)
    }

    /**
     * Bkav HuyNgQe: play random
     */
    fun playRandom(){
        checkShuffle = true
        val sumIndexSong: Int = listSong.value!!.size - 1
        val indexRandom = (0..sumIndexSong).random()
        val song: Song = listSong.value!!.get(indexRandom)
        intent.putExtra(DATA_SHUFFLE, (indexRandom).toString())
        sendBroadcast(intent)
        playMusic(song)
    }

 /**
  * Bkav HuyNgQe: auto next song
  */
 fun nextSongAuto(index: Int) {
     // send song
     intent.putExtra(DATA, (index).toString())
     sendBroadcast(intent)
     playMusic(listSong.value!!.get(index))
     mediaPlayer.setOnCompletionListener { onCompletion(mediaPlayer) }
 }
    /**
     * Bkav HuyNgQe: next song do bam nut next
     */
    fun nextSong(song: Song?){
        if (song != null) {
           val indexNext:Int = listSong.value!!.indexOf(song)
            nextSongAuto(indexNext)
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
    fun startMusic(){
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
/**
 * Bkav HuyNgQe:gowx bound service
 */

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
 /*****************************logicTime*********************/
    /**
     * Bkav HuyNgQe: time duration
     */
    fun timeDuration(song:Song): String{
        song.duration.let {
            val timeS = (it?.div(1000))?.toInt()
            val min = (timeS?.div(60))
            val sec = (timeS?.rem(60))
            totalDuration = resources.getString(R.string.total_duration, min, sec)
        }
        return  totalDuration
    }
    /**
     * Bkav HuyNgQe: format current time
     */
    fun formatCurrentTime():String{
        val timeCurrent: Int? = getCurrentTime()
        val timeS = (timeCurrent?.div(1000))
        val min = (timeS?.div(60))
        val sec = (timeS?.rem(60))
        currentTime = resources.getString(R.string.total_duration,min, sec)
        return currentTime
    }
}
