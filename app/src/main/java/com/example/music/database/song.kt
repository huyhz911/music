package com.example.music.database

import java.time.Duration


data class song (
  var songID: Long =0L,
  val songName: String,
  val albumName: String,
  val duration: Long =0L
        )