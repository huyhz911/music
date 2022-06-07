package com.example.music
import android.app.SearchManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.music.databinding.ActivityMainBinding

class ActivityMusic : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
       val window: Window = this@ActivityMusic.window
        window.statusBarColor= ContextCompat.getColor(this@ActivityMusic,R.color.statusbar)

    }


}