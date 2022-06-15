package com.example.music

import android.app.Application
import android.content.Context

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object {
        var instance: MyApplication? = null
        fun getContext(): Context{
            return instance!!.applicationContext
        }
    }
}