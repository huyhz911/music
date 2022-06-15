package com.example.music.allSongs


import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.LocalMusicDataSource
import com.example.music.databinding.AllSongsFragmentBinding
// líst template , author file ,
/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class  AllSongsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding= DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,R.layout.all_songs_fragment,container,false)
        val dataSource = LocalMusicDataSource()
        val viewModelFactory = AllSongViewModelFactory(dataSource)
        val allSongViewModel = ViewModelProvider(this, viewModelFactory).get(AllSongViewModel::class.java)
        binding.lifecycleOwner= this
        binding.allSongsViewModel = allSongViewModel

        val adapter = SongAdapter(SongListener { songId -> allSongViewModel.playMusic(songId)  })
        binding.listSong.adapter = adapter
        allSongViewModel.listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        binding.relativeLayout.setOnClickListener { view: View -> view.findNavController().navigate(AllSongsFragmentDirections.actionAllSongsFragmentToMediaPlaybackFragment2()) }

        return  binding.root

    }



}
