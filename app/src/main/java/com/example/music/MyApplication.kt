package com.example.music

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        createChannelNotification()
    }
/**
 * Bkav HuyNgQe: tao kenh thong bao
 */
    private fun createChannelNotification() {
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val channel = NotificationChannel(CHANNEL_ID,
               name,
               NotificationManager.IMPORTANCE_HIGH )
           channel.setSound(null, null)
           val manager: NotificationManager = getSystemService(NotificationManager::class.java)
           manager.createNotificationChannel(channel)
       }
    }


    companion object {
        var instance: MyApplication? = null
        fun getContext(): Context{
            return instance!!.applicationContext
        }
        const val CHANNEL_ID: String = "notification_service"
        const val name: String = "channel_notification_service"
    }
}