package com.example.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.music.database.Song
import com.example.music.databinding.ActivityMainBinding
import com.example.music.mediaPlayService.MediaPlaybackService

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


    }

    public lateinit var mService: MediaPlaybackService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlaybackService.LocalBinder
            mService = binder.getService()
            listSong.value = mService.listSong.value
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, MediaPlaybackService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

}