package com.example.music.mediaPlay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import com.example.music.R
import com.example.music.databinding.AllSongsFragmentBinding
import com.example.music.databinding.MediaPlayBackFragmentBinding

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
        binding.backListSong.setOnClickListener { view: View -> view.findNavController().navigate(MediaPlaybackFragmentDirections.actionMediaPlaybackFragmentToAllSongsFragment())}
        return binding.root
    }
}