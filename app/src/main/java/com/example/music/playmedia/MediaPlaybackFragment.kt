package com.example.music.playmedia

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
import com.example.music.RepeatStatus
import com.example.music.showallsongs.SongAdapter
import com.example.music.showallsongs.SongListener
import com.example.music.database.FavoriteSongsDatabase
import com.example.music.database.Song
import com.example.music.databinding.MediaPlayBackFragmentBinding
import com.example.music.service.MediaPlaybackService

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackFragment: Fragment() {
    private lateinit var favoriteSongsViewModel: MediaPlaybackViewModel
    private lateinit var binding: MediaPlayBackFragmentBinding
    val handler = Handler()
    companion object{
        private const val SONG_UPDATE_UI ="song update ui"
        private const val DATA ="data"
        private const val DATA_REPEAT ="data repeat"
        private const val DATA_SHUFFLE ="data shuffle"
    }

    /**
     * Bkav HuyNgQe: nhan data tu service de auto next song
     */
    private var mBroadcast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (SONG_UPDATE_UI == intent?.action) {
                /*Bkav HuyNgQe: nhan data bai hat khi auto next vaf update ui */
                val index: String? = intent.getStringExtra(DATA)
                val song: Song? =
                    index?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt()) }
                if (song != null) {
                    updateUISong(song)
                    updateTimeDuration(song)
                    updateTimeCurrent(song)
                }
                /*Bkav HuyNgQe:  nhan data bai hat khi next shuffle  vaf update ui */
                val indexShuffle: String? = intent.getStringExtra(DATA_SHUFFLE)
                val songShuffle: Song? =
                    indexShuffle?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt()) }
                if (songShuffle != null) {
                    updateUISong(songShuffle)
                    updateTimeDuration(songShuffle)
                    updateTimeCurrent(songShuffle)
                }
                /*Bkav HuyNgQe: nhan data bai hat khi next repeat  vaf update ui  */
                val indexRepeat: String? = intent.getStringExtra(DATA_REPEAT)
                val songRepeat: Song? =
                    indexRepeat?.let { (activity as ActivityMusic).listSong.value?.get(it.toInt()) }
                if (songRepeat != null) {
                    updateUISong(songRepeat)
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
        val dataSource =
            FavoriteSongsDatabase.getInstance(MyApplication.getContext()).favoriteSongsDatabaseDAO
        val viewModelFactory = MediaPlaybackViewModelFactory(dataSource)
        favoriteSongsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MediaPlaybackViewModel::class.java)
        binding.mediaPlayViewModel = favoriteSongsViewModel
        binding.resources = resources
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
        updateUISong(getArgs())
        /*Bkav HuyNgQe:  cập nhật trạng thái nút play */
        binding.togglePlayPause.setOnClickListener {
            if (binding.togglePlayPause.isChecked) {
                (activity as ActivityMusic).mService?.startMusic()
            } else {
                (activity as ActivityMusic).mService?.pauseMusic()
            }
        }
        /*Bkav HuyNgQe: update time duration */
        updateTimeDuration(getArgs())
        updateTimeCurrent(getArgs())
        /*Bkav HuyNgQe:chuyen bai hat khi bam nut next */
        binding.nextButton.setOnClickListener {
            val index: Int = MediaPlaybackService.index
            val listSize: Int? = ((activity as ActivityMusic).mService?.listSong?.value?.size)
            if ((index + 1) == listSize) {
                val song: Song? = (activity as ActivityMusic).mService?.listSong?.value?.get(0)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(0)
                    updateUISong(song)
                    updateTimeCurrent(song)
                    updateTimeDuration(song)
                }
            } else {
                val song: Song? =
                    (activity as ActivityMusic).mService?.listSong?.value?.get(index + 1)
                (activity as ActivityMusic).mService?.nextSong(song)
                if (song != null) {
                    updateUISong(song)
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
            if (index == 0) {
                val song: Song? =
                    (activity as ActivityMusic).mService?.listSong?.value?.get(listSize!! - 1)
                if (song != null) {
                    (activity as ActivityMusic).mService?.nextSongAuto(listSize!! - 1)
                    updateUISong(song)
                    updateTimeCurrent(song)
                    updateTimeDuration(song)
                }
            } else {
                if ((activity as ActivityMusic).mService?.getCurrentTime()!! < 3000) {
                    val songBack: Song? =
                        (activity as ActivityMusic).mService?.listSong?.value?.get(index!! - 1)
                    (activity as ActivityMusic).mService?.nextSongAuto(index - 1)
                    if (songBack != null) {
                        updateUISong(songBack)
                        updateTimeCurrent(songBack)
                        updateTimeDuration(songBack)
                    }
                } else {
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
            (activity as ActivityMusic).mService?.repeatStatus = RepeatStatus.REPEAT_ON
            binding.imageOffRepeat.visibility = View.GONE
            binding.imageRepeatOn.visibility = View.VISIBLE
            (activity as ActivityMusic).mService?.repeatOn()
        }
        /*Bkav HuyNgQe: turn on repeat one */
        binding.imageRepeatOn.setOnClickListener {
            (activity as ActivityMusic).mService?.repeatStatus = RepeatStatus.REPEAT_ONE
            binding.imageRepeatOn.visibility = View.GONE
            binding.imageRepeatOne.visibility = View.VISIBLE
            (activity as ActivityMusic).mService?.repeatOne()
        }
        /*Bkav HuyNgQe: turn off repeat one */
        binding.imageRepeatOne.setOnClickListener {
            (activity as ActivityMusic).mService?.repeatStatus = RepeatStatus.REPEAT_OFF
            binding.imageRepeatOne.visibility = View.GONE
            binding.imageOffRepeat.visibility = View.VISIBLE
            val index = MediaPlaybackService.index + 1
            (activity as ActivityMusic).mService?.nextSongAuto(index)
        }
        /*HuyNgQe: xu ly su kien nut like*/
        binding.toggleLike.setOnClickListener {
            if (binding.toggleLike.isChecked) {
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
    private fun updateUISong(song: Song) {
        favoriteSongsViewModel.updateUiSong(song)
    }

    /**
     * Bkav HuyNgQe:update time duration
     */
    fun updateTimeDuration(song: Song) {
        binding.textTimeDuration.text = (activity as ActivityMusic).mService?.timeDuration(song)
    }

    /**
     * Bkav HuyNgQe: update timeCurrent
     */
    fun updateTimeCurrent(song: Song) {
        /*Bkav HuyNgQe: update seek bar */
        binding.seekBarMediaPlay.max = song.duration!!.toInt()
        binding.seekBarMediaPlay.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
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
                    binding.textTimeCurrent.text =
                        (activity as ActivityMusic).mService?.formatCurrentTime()
                    binding.seekBarMediaPlay.progress =
                        (it as ActivityMusic).mService?.mediaPlayer!!.currentPosition
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    /**
     * Bkav HuyNgQe:khi onStart thi bo dang ki broadcast
     */
    override fun onStart() {
        super.onStart()
        /*Bkav HuyNgQe:  đăng kí broadcast */
        val intentFilter = IntentFilter(SONG_UPDATE_UI)
        requireActivity().registerReceiver(mBroadcast, intentFilter);
    }

    /**
     * Bkav HuyNgQe:khi onstop thi bo dang ki broadcast
     */
    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(mBroadcast)
    }
}