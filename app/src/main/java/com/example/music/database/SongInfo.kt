package com.example.music.database

class SongInfo( private  var songID: Long,
                private  var songName: String,
                private  var albumName: String,
                private  var artists: String,
                private  var duration: Long ) {
fun setTitle(songName: String) {
    this.songName = songName
}
    fun setAlbumId(songID: Long) {
        this.songID = songID
    }
    fun setAlbumName(albumName: String){
        this.albumName = albumName
    }
    fun setArtist( artists: String){
        this.artists = artists
    }
    fun setDuration(duration: Long){
        this.duration =duration
    }




}