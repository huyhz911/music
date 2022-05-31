package com.example.music.allSongs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.music.databinding.AllSongsFragmentBinding
import com.example.music.R

class AllSongsFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding= DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,R.layout.all_songs_fragment,container,false)
        return  binding.root
    }
}