package com.example.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.music.service.MediaPlaybackService

/**
 * Created by Bkav HuyNgQe on 12/07/2022.
 */
class BroadcastReceiverNotification: BroadcastReceiver() {
    private val ACTION_NOTIFICATION = "action notification"
    override fun onReceive(context: Context?, intent: Intent?) {
        val action: Int = intent!!.getIntExtra(ACTION_NOTIFICATION, 0)
        val intentAction = Intent(context, MediaPlaybackService::class.java)
        intentAction.putExtra(ACTION_NOTIFICATION, action)
        context?.startService(intentAction)
    }
}