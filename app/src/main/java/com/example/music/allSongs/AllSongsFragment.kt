package com.example.music.allSongs

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.SongRepository
import com.example.music.databinding.AllSongsFragmentBinding
import com.example.music.mediaPlayService.MediaPlaybackService

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class  AllSongsFragment: Fragment() {

     var songID: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,
            R.layout.all_songs_fragment,
            container,
            false)
        val mediaPlaybackService = MediaPlaybackService()
        val songRepository = SongRepository()
        binding.lifecycleOwner = this
        // su ly su kien khi click vao song item
        val adapter = SongAdapter(SongListener { songId ->
            // start service
            val intent = Intent(MyApplication.getContext(), MediaPlaybackService::class.java)
            activity?.startService(intent)

            mediaPlaybackService.playMusic(songId)
            binding.imageAlbum.setImageBitmap(songRepository.getCoverPicture(songId))
            binding.textSongName.text = songRepository.getSongName(songId)
            binding.textAuthor.text = songRepository.getSongAuthor(songId)
            binding.songPopUp.visibility = View.VISIBLE
            binding.togglePlayPause.isChecked = true
            songID = songId
        })
        binding.listSong.adapter = adapter
//        allSongViewModel.listSong.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//            }
//        })

        (activity as ActivityMusic).listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        // chuyen doi so sang cho media play fragment
        binding.relativeLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(AllSongsFragmentDirections.actionAllSongsFragmentToMediaPlaybackFragment2(songID))
        }

//        (activity as ActivityMusic).mService
        return  binding.root
    }


}
