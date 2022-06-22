package com.example.music.mediaPlay

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.music.R
import com.example.music.allSongs.SongAdapter
import com.example.music.allSongs.SongListener
import com.example.music.database.SongRepository
import com.example.music.databinding.MediaPlayBackFragmentBinding
import com.example.music.mediaPlayService.MediaPlaybackService

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class MediaPlaybackFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding= DataBindingUtil.inflate<MediaPlayBackFragmentBinding>(inflater,
            R.layout.media_play_back_fragment,container,false)
        binding.imageBackListSong?.setOnClickListener { view: View -> view.findNavController().navigate(MediaPlaybackFragmentDirections.actionMediaPlaybackFragmentToAllSongsFragment())}
        val mediaPlaybackService = MediaPlaybackService()
        val songRepository = SongRepository()
        binding.lifecycleOwner = this
        val adapter = SongAdapter(SongListener { })
        binding.listSong?.adapter = adapter
        mediaPlaybackService.listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        // set background
        binding.mediaPlayback?.background = BitmapDrawable(resources,songRepository.getCoverPicture(getArgs()))
        // set backgroundlandscape
        binding.mediaPlayBackLandScape?.background = BitmapDrawable(resources,songRepository.getCoverPicture(getArgs()))
        // set image popUp
        binding.imageAlbumMediaPlay.setImageBitmap(songRepository.getCoverPicture(getArgs()))
        //set name song
        binding.textSongNameMediaPlay.text = songRepository.getSongName(getArgs())
        //set song
        binding.textAuthorMediaPlay.text = songRepository.getSongAuthor(getArgs())
        return binding.root
    }
    /**
     * Bkav HuyNgQe: lay doi so chuyen tu all song fragment
     */
    private fun getArgs(): Int {
        val args = MediaPlaybackFragmentArgs.fromBundle(requireArguments())
        return args.songID
    }
}