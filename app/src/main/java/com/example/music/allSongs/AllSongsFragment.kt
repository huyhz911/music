package com.example.music.allSongs

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.music.R
import com.example.music.database.LocalMusicDataSoure
import com.example.music.databinding.AllSongsFragmentBinding


class AllSongsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding= DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,R.layout.all_songs_fragment,container,false)
        val adapter = SongAdapter()
        binding.listSong.adapter = adapter
        val dataSource = LocalMusicDataSoure()
        val viewModelFactory = AllSongViewModelFactory(dataSource)
        val sleepQualityViewModel = ViewModelProvider(this, viewModelFactory).get(AllSongViewModel::class.java)
        return  binding.root

    }



}