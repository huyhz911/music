package com.example.music

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object {
        var instance: MyApplication? = null
    }
}