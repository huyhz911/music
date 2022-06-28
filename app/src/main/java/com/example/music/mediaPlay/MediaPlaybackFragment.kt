package com.example.music.mediaPlay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.R
import com.example.music.allSongs.SongAdapter
import com.example.music.allSongs.SongListener
import com.example.music.database.Song
import com.example.music.databinding.MediaPlayBackFragmentBinding

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackFragment: Fragment() {
    private lateinit var binding: MediaPlayBackFragmentBinding
    companion object{
        private const val SONG_UPDATE_UI ="send song"
        private const val DATA ="data"
    }
    var check: Boolean = false
    /**
     * Bkav HuyNgQe: nhan data tu service de auto next song
     */
    private var mBroadcast = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            check = SONG_UPDATE_UI == intent?.action
            if (check){
                val index: String? = intent?.getStringExtra(DATA)
                val song: Song? = index?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt())}
                if (song != null) {
                    updateSong(song)
                    updateTimeDuration(song)
                    updateTimeCurrent()
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater,
            R.layout.media_play_back_fragment,container,false)
        binding.imageBackListSong?.setOnClickListener { view: View -> view.findNavController().navigate(MediaPlaybackFragmentDirections.actionMediaPlaybackFragmentToAllSongsFragment())}
        binding.lifecycleOwner = this
        val adapter = SongAdapter(SongListener { })
        binding.listSong?.adapter = adapter

        (activity as ActivityMusic).listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        if ((activity as ActivityMusic).mService?.checkIsPlaying() == true){
            binding.togglePlayPause.isChecked = true
        }
        /*Bkav HuyNgQe:update song */
        updateSong(getArgs())
        /*Bkav HuyNgQe:  cập nhật trạng thái nút play */
        binding.togglePlayPause.setOnClickListener {   if (binding.togglePlayPause.isChecked){
            (activity as ActivityMusic).mService?.resumeMusic()

        }else{
            (activity as ActivityMusic).mService?.pauseMusic()
           }
        }
        /*Bkav HuyNgQe:  đăng kí broadcast */
        val intentFilter= IntentFilter(SONG_UPDATE_UI)
        requireActivity().registerReceiver(mBroadcast,intentFilter);
       /*Bkav HuyNgQe: update time duration */
        updateTimeDuration(getArgs())
        /*Bkav HuyNgQe: update seek bar */
        binding.seekBarMediaPlay.max = getArgs().duration!!.toInt()
        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                binding.seekBarMediaPlay.progress = (activity as ActivityMusic).mService?.mediaPlayer!!.currentPosition
                handler.postDelayed(this,100)
            }

        },0)
        /*Bkav HuyNgQe:  update time current */
        handler.postDelayed(object :Runnable{
            override fun run() {
                updateTimeCurrent().let {
                    val timeS = (it?.div(1000))?.toInt()
                    val min = (timeS?.div(60))
                    val sec = (timeS?.rem(60))
                    binding.textTimeCurrent.text = ("$min: $sec").toString()
                }
                handler.postDelayed(this,1000)
            }
        },0)
        binding.seekBarMediaPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    (activity as ActivityMusic).mService?.mediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        /*Bkav HuyNgQe:chuyen bai hat khi bam nut next */
        binding.nextButton.setOnClickListener {
            val index: Int? = (activity as ActivityMusic).mService?.index
            val listSize: Int? = ((activity as ActivityMusic).mService?.listSong?.value?.size)
            if (  (index!! + 1) == listSize){
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(0)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(0)
                    updateSong(song)
                    updateTimeCurrent()
                    updateTimeDuration(song)
                }
            }else{
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(index!! + 1)
                (activity as ActivityMusic).mService?.nextSong(song)
                if (song != null) {
                    updateSong(song)
                    updateTimeCurrent()
                    updateTimeDuration(song)
                }
            }
         }
        binding.backButton.setOnClickListener {
            val index: Int? = (activity as ActivityMusic).mService?.index
            val listSize: Int? = ((activity as ActivityMusic).mService?.listSong?.value?.size)
            if(index == 0){
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(listSize!! -1)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(listSize!! -1)
                    updateSong(song)
                    updateTimeCurrent()
                    updateTimeDuration(song)
                }
            }else{
                if (updateTimeCurrent()!! < 3000 ){
                    val songBack: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(index!! - 1)
                    if (index != null) {
                        (activity as ActivityMusic).mService?.nextSongAuto(index - 1)
                    }
                    if (songBack != null) {
                        updateSong(songBack)
                        updateTimeCurrent()
                        updateTimeDuration(songBack)
                    }
                }else{
                    if (index != null) {
                        (activity as ActivityMusic).mService?.nextSongAuto(index)
                    }
                }
            }

        }
        return binding.root
    }
    /**
     * Bkav HuyNgQe: lay doi so chuyen tu all song fragment
     */
    private fun getArgs(): Song {
       return MediaPlaybackFragmentArgs.fromBundle(requireArguments()).song
    }
    /**
     * Bkav HuyNgQe: update ui music
     */
    private fun updateSong(song: Song){
        // set background
        binding.mediaPlayback?.background = BitmapDrawable(resources,song.getPicture())
        // set backgroundlandscape
        binding.mediaPlayBackLandScape?.background = BitmapDrawable(resources,song.getPicture())
        // set image popUp
        binding.imageAlbumMediaPlay.setImageBitmap(song.getPicture())
        //set name song
        binding.textSongNameMediaPlay.text = song.songName
        //set song
        binding.textAuthorMediaPlay.text = song.artists
    }
    /**
     * Bkav HuyNgQe:update time duration
     */
    fun updateTimeDuration(song:Song){
        song.duration.let {
            val timeS = (it?.div(1000))?.toInt()
            val min = (timeS?.div(60))
            val sec = (timeS?.rem(60))
            binding.textTimeDuration.text = ("$min: $sec").toString()
        }
    }
    /**
     * Bkav HuyNgQe: update timeCurrent
     */
    fun updateTimeCurrent(): Int? {
        return (activity as ActivityMusic).mService?.mediaPlayer?.currentPosition
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(mBroadcast)
    }
}