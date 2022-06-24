package com.example.music.allSongs

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.Song
import com.example.music.database.SongRepository
import com.example.music.databinding.AllSongsFragmentBinding
import com.example.music.mediaPlayService.MediaPlaybackService
import java.io.Serializable

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class  AllSongsFragment: Fragment() {

    lateinit var songPra :Song
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,
            R.layout.all_songs_fragment,
            container,
            false)

        val songRepository = SongRepository()
        binding.lifecycleOwner = this


        // su ly su kien khi click vao song item
        val adapter = SongAdapter(SongListener { song ->
            // start service
       //     val intent = Intent(activity, MediaPlaybackService::class.java)
//            val bundle= Bundle()
//            val picture = GetSongPicture(song)
//            bundle.putSerializable("pictureSong", picture)
//            intent.putExtras(bundle)
//            activity?.startService(intent)
            val mediaPlaybackService =  (activity as ActivityMusic).mService
            mediaPlaybackService?.sendNotification(song)
            mediaPlaybackService?.playMusic(song)
            binding.imageAlbum.setImageBitmap(songRepository.getCoverPicture(song))
            binding.textSongName.text = songRepository.getSongName(song)
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

//        (activity as ActivityMusic).mService
        return  binding.root
    }


}

