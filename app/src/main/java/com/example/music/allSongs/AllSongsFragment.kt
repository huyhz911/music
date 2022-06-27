package com.example.music.allSongs

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
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.R
import com.example.music.database.Song
import com.example.music.databinding.AllSongsFragmentBinding


/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class  AllSongsFragment: Fragment() {

    lateinit var songPra :Song
    lateinit var binding: AllSongsFragmentBinding
    companion object{
        private const val SONG_ACTION ="send song"
        private const val DATA ="data"
    }
    var check: Boolean = false
    /**
     * Bkav HuyNgQe:
     */
    private var mBroadcast = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            check = SONG_ACTION == intent?.action
            if (check){
                val index: String? = intent?.getStringExtra(DATA)
                if (index != null) {
                   updateSong(index.toInt())
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
        binding.lifecycleOwner = this


        // su ly su kien khi click vao song item
        val adapter = SongAdapter(SongListener { song ->
            val mediaPlaybackService =  (activity as ActivityMusic).mService
            mediaPlaybackService?.sendNotification(song)
            mediaPlaybackService?.playMusic(song)
            binding.imageAlbum.setImageBitmap(song.getPicture())
            binding.textSongName.text = song.songName
            binding.textAuthor.text = song.artists
            binding.songPopUp.visibility = View.VISIBLE
            binding.togglePlayPause.isChecked = true
            songPra = song
        })
        binding.listSong.adapter = adapter

        (activity as ActivityMusic).listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        // chuyen doi so sang cho media play fragment
        binding.relativeLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(AllSongsFragmentDirections.actionAllSongsFragmentToMediaPlaybackFragment2(songPra))
        }
        binding.togglePlayPause.setOnClickListener { if (binding.togglePlayPause.isChecked){
            (activity as ActivityMusic).mService?.resumeMusic()

        }else{
            (activity as ActivityMusic).mService?.pauseMusic()

         }
        }
        val intentFilter= IntentFilter(SONG_ACTION)
        requireActivity().registerReceiver(mBroadcast,intentFilter);

        return  binding.root
    }

    fun updateSong(index: Int){
            val songNext: Song = (activity as ActivityMusic).listSong.value!!.get(index)
            binding.imageAlbum.setImageBitmap(songNext.getPicture())
            binding.textSongName.text = songNext.songName
            binding.textAuthor.text = songNext.artists

    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(mBroadcast)
    }


}

