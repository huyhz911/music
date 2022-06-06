package com.example.music.allSongs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.music.database.SongInfo
import com.example.music.R
import com.example.music.databinding.ListItemSongBinding

class SongAdapter: ListAdapter<SongInfo,SongAdapter.SongViewHolder>(SongInfoDiffCallback()) {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val item = getItem(position)
        holder.songName.text = item.getTitle()
        holder.songPosition.text = (position + 1).toString()
        holder.songDuration.text = item.getDuration().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSongBinding.inflate(layoutInflater,parent,false)
        return SongViewHolder(binding)

    }
    class SongViewHolder(binding: ListItemSongBinding): RecyclerView.ViewHolder(binding.root){
        val songPosition : TextView = binding.position
        val songName: TextView = binding.songName
        val songDuration: TextView = binding.duration

    }
}
class SongInfoDiffCallback: DiffUtil.ItemCallback<SongInfo>(){
    override fun areItemsTheSame(oldItem: SongInfo, newItem: SongInfo): Boolean {
        return  oldItem.getAlbumId() == newItem.getAlbumId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SongInfo, newItem: SongInfo): Boolean {
        return oldItem == newItem
    }

}

