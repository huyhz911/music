package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.music.databinding.ActivityMainBinding

class ActivityMusic : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_main)
    }
}