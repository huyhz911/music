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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.allSongs.SongAdapter
import com.example.music.allSongs.SongListener
import com.example.music.database.FavoriteSongs
import com.example.music.database.FavoriteSongsDatabase
import com.example.music.database.Song
import com.example.music.databinding.MediaPlayBackFragmentBinding
import com.example.music.mediaPlayService.MediaPlaybackService

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackFragment: Fragment() {
    private lateinit var favoriteSongsViewModel: MediaPlaybackViewModel
    private lateinit var binding: MediaPlayBackFragmentBinding
    val handler = Handler()
    companion object{
        private const val SONG_UPDATE_UI ="send song"
        private const val DATA ="data"
        private const val DATA_REPEAT ="send song repeat"
        private const val DATA_SHUFFLE ="data shuffle"
    }
    var check: Boolean = false
    /**
     * Bkav HuyNgQe: nhan data tu service de auto next song
     */
    private var mBroadcast = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            check = SONG_UPDATE_UI == intent?.action
            if (check){
                /*Bkav HuyNgQe: nhan data bai hat khi auto next vaf update ui */
                val index: String? = intent?.getStringExtra(DATA)
                val song: Song? = index?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt())}
                if (song != null) {
                    updateSong(song)
                    updateTimeDuration(song)
                    updateTimeCurrent(song)
                }
                /*Bkav HuyNgQe:  nhan data bai hat khi next shuffle  vaf update ui */
                val indexShuffle: String? = intent?.getStringExtra(DATA_SHUFFLE)
                val songShuffle: Song? = indexShuffle?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt())}
                if (songShuffle != null) {
                    updateSong(songShuffle)
                    updateTimeDuration(songShuffle)
                    updateTimeCurrent(songShuffle)
                }
                /*Bkav HuyNgQe: nhan data bai hat khi next repeat  vaf update ui  */
                val indexRepeat: String? = intent?.getStringExtra(DATA_REPEAT)
                val songRepeat: Song? = indexRepeat?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt())}
                if (songRepeat != null) {
                    updateSong(songRepeat)
                    updateTimeDuration(songRepeat)
                    updateTimeCurrent(songRepeat)
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
        /*Bkav HuyNgQe: submit item len recyclerView */
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
         updateTimeCurrent(getArgs())
        /*Bkav HuyNgQe:chuyen bai hat khi bam nut next */
        binding.nextButton.setOnClickListener {
            val index: Int = MediaPlaybackService.index
            val listSize: Int? = ((activity as ActivityMusic).mService?.listSong?.value?.size)
            if (  (index + 1) == listSize){
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(0)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(0)
                    updateSong(song)
                    updateTimeCurrent(song)
                    updateTimeDuration(song)
                }
            }else{
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(index + 1)
                (activity as ActivityMusic).mService?.nextSong(song)
                if (song != null) {
                    updateSong(song)
                    updateTimeCurrent(song)
                    updateTimeDuration(song)
                }
            }
         }
        /*Bkav HuyNgQe: lo gic back button
        *   neu thoi gian hien tai < 3s thi quay ve bai truoc
        * neu thoi gian hien tai >3 play lai bai nhac do
        *  */
        binding.backButton.setOnClickListener {
            val index: Int = MediaPlaybackService.index
            val listSize: Int? = ((activity as ActivityMusic).mService?.listSong?.value?.size)
            if(index == 0){
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(listSize!! -1)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(listSize!! -1)
                    updateSong(song)
                    updateTimeCurrent(song)
                    updateTimeDuration(song)
                }
            }else{
                if ((activity as ActivityMusic).mService?.getCurrentTime()!! < 3000 ){
                    val songBack: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(index!! - 1)
                        (activity as ActivityMusic).mService?.nextSongAuto(index - 1)
                    if (songBack != null) {
                        updateSong(songBack)
                        updateTimeCurrent(songBack)
                        updateTimeDuration(songBack)
                    }
                }else{
                        (activity as ActivityMusic).mService?.nextSongAuto(index)
                }
            }

        }



        /*Bkav HuyNgQe: lo gic nut shuffle */
        binding.imageShuffleOff.setOnClickListener {
                binding.imageShuffleOff.visibility = View.GONE
                binding.imageShuffleOn.visibility = View.VISIBLE
                (activity as ActivityMusic).mService?.playRandom()
        }
        /*Bkav HuyNgQe: turn off shuffle */
        binding.imageShuffleOn.setOnClickListener {
            binding.imageShuffleOff.visibility = View.VISIBLE
            binding.imageShuffleOn.visibility = View.GONE
            (activity as ActivityMusic).mService?.checkShuffle = false
            (activity as ActivityMusic).mService?.mediaPlayer?.setOnCompletionListener {
                val index: Int = MediaPlaybackService.index
                    (activity as ActivityMusic).mService?.nextSongAuto(index + 1)
            }
        }
        /*Bkav HuyNgQe: turn on repeat */
        binding.imageOffRepeat.setOnClickListener {
            (activity as ActivityMusic).mService?.checkRepeatOff = false
            binding.imageOffRepeat.visibility = View.GONE
            binding.imageRepeatOn.visibility = View.VISIBLE
            (activity as ActivityMusic).mService?.repeatOn()
        }
        /*Bkav HuyNgQe: turn on repeat one */
        binding.imageRepeatOn.setOnClickListener {
            binding.imageRepeatOn.visibility = View.GONE
            binding.imageRepeatOne.visibility = View.VISIBLE
            (activity as ActivityMusic).mService?.repeatOne()
        }
        /*Bkav HuyNgQe: turn off repeat one */
        binding.imageRepeatOne.setOnClickListener {
            (activity as ActivityMusic).mService?.checkRepeatOff = true
            binding.imageRepeatOne.visibility = View.GONE
            binding.imageOffRepeat.visibility = View.VISIBLE
            (activity as ActivityMusic).mService?.checkRepeatOne = false
            val index =MediaPlaybackService.index + 1
            (activity as ActivityMusic).mService?.nextSongAuto(index)
        }

       /*HuyNgQe: xu ly su kien nut like*/
        val dataSource = FavoriteSongsDatabase.getInstance(MyApplication.getContext()).favoriteSongsDatabaseDAO
        val viewModelFactory = MediaPlaybackViewModelFactory(dataSource)
        favoriteSongsViewModel = ViewModelProvider(this, viewModelFactory).get(MediaPlaybackViewModel::class.java)
        binding.mediaPlayViewModel = favoriteSongsViewModel
        binding.toggleLike.setOnClickListener {
            if (binding.toggleLike.isChecked){
                favoriteSongsViewModel.onClickLike()
            }
        }
       /*HuyNgQe: xu ly su kien nut disLike*/
       binding.toggleDislike.setOnClickListener {
           favoriteSongsViewModel.onClickDislike()
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
            binding.textTimeDuration.text = (activity as ActivityMusic).mService?.timeDuration(song)
    }
    /**
     * Bkav HuyNgQe: update timeCurrent
     */
    fun updateTimeCurrent(song:Song) {
        /*Bkav HuyNgQe: update seek bar */
        binding.seekBarMediaPlay.max = song.duration!!.toInt()
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
        /*Bkav HuyNgQe:  update time current */
        handler.postDelayed(object : Runnable {
            override fun run() {
                activity?.let {
                    binding.textTimeCurrent.text =(activity as ActivityMusic ).mService?.formatCurrentTime()
                    binding.seekBarMediaPlay.progress =
                        (it as ActivityMusic).mService?.mediaPlayer!!.currentPosition
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

/**
 * Bkav HuyNgQe:khi onstop thi bo dang ki broadcast
 */
    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(mBroadcast)
    }
}