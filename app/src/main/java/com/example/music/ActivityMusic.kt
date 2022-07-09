package com.example.music

import android.app.ActivityManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.music.database.Song
import com.example.music.databinding.ActivityMainBinding
import com.example.music.service.MediaPlaybackService


/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class ActivityMusic : AppCompatActivity() {
    var listSong = MutableLiveData<ArrayList<Song>>()

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = this@ActivityMusic.window
        window.statusBarColor = ContextCompat.getColor(this@ActivityMusic, R.color.statusbar)

        /*Bkav HuyNgQe:  Bind to LocalService */
        Intent(this, MediaPlaybackService::class.java).also { intent ->
            if (!isServiceRunning(MediaPlaybackService::class.java.name)) {
                startService(intent)
            }
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    public var mService: MediaPlaybackService? = null
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlaybackService.LocalBinder
            mService = binder.getService()
            listSong.value = mService?.listSong?.value
            mBound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(connection)
            mBound=false
        }
    }

    private fun isServiceRunning(serviceClassName: String?): Boolean {
        val activityManager =
            MyApplication.getContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services: List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(Int.MAX_VALUE)
        for (runningServiceInfo in services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true
            }
        }
        return false
    }
}