package com.example.music.allSongs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.R
import com.example.music.database.LocalMusicDataSource
import com.example.music.database.SongInfo
import com.example.music.databinding.AllSongsFragmentBinding
// líst template , author file ,

class AllSongsFragment: Fragment() {

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
        val adapter = SongAdapter()
        binding.listSong.adapter = adapter
        allSongViewModel.listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return  binding.root

    }



}
