package com.example.music.showallsongs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.FavoriteSongsDatabase
import com.example.music.database.Song
import com.example.music.databinding.AllSongsFragmentBinding

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class AllSongsFragment : Fragment() {
    private lateinit var allSongsViewModel: AllSongsViewModel
    lateinit var songPra: Song
    lateinit var binding: AllSongsFragmentBinding
    companion object {
        private const val SONG_UPDATE_UI = "song update ui"
        private const val DATA = "data"
    }
    /**
     * Bkav HuyNgQe: nhan data tu service
     */
    private var mBroadcast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (SONG_UPDATE_UI == intent?.action) {
                val index: String? = intent.getStringExtra(DATA)
                if (index != null) {
                    updateUISong(index.toInt())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.all_songs_fragment,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner

        /*Bkav HuyNgQe: su ly su kien khi click vao song item */
        val dataSource =
            FavoriteSongsDatabase.getInstance(MyApplication.getContext()).favoriteSongsDatabaseDAO
        val allSongViewModelFactory = AllSongsViewModelFactory(dataSource)
        allSongsViewModel =
            ViewModelProvider(this, allSongViewModelFactory).get((AllSongsViewModel::class.java))
        binding.allSongsViewModel = allSongsViewModel
        val adapter = SongAdapter(SongListener { song ->
            val mediaPlaybackService = (activity as ActivityMusic).mService
            allSongsViewModel.insert(song)
            if (mediaPlaybackService != null) {
                mediaPlaybackService.listSong.value?.let {
                    mediaPlaybackService.nextSongAuto(it.indexOf(song))
                }
                mediaPlaybackService.listSong.value?.let { updateUISong(it.indexOf(song)) }
            }
            binding.songPopUp.visibility = View.VISIBLE
            binding.togglePlayPause.isChecked = true
            songPra = song
            mediaPlaybackService?.sendNotification(song)
        })
        binding.listSong.adapter = adapter

        (activity as ActivityMusic).listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        /*Bkav HuyNgQe: chuyen doi so sang cho media play fragment */
        binding.relativeLayout.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(AllSongsFragmentDirections.actionAllSongsFragmentToMediaPlaybackFragment2(
                    songPra))
        }
        /*Bkav HuyNgQe: s??? ki???n n??t play */
        binding.togglePlayPause.setOnClickListener {
            if (binding.togglePlayPause.isChecked) {
                (activity as ActivityMusic).mService?.startMusic()

            } else {
                (activity as ActivityMusic).mService?.pauseMusic()
            }
        }
        /*Bkav HuyNgQe: ????ng k?? broadcast  */
        val intentFilter = IntentFilter(SONG_UPDATE_UI)
        requireActivity().registerReceiver(mBroadcast, intentFilter);
        return binding.root
    }
    /**
     * Bkav HuyNgQe: update thong tin bai hat
     */
    fun updateUISong(index: Int) {
        val songNext: Song = (activity as ActivityMusic).listSong.value!!.get(index)
        allSongsViewModel.updateUiSong(songNext)
    }
    /**
     * Bkav HuyNgQe: g??? ????ng k?? broadcast khi app stop
     */
    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(mBroadcast)
    }
}

