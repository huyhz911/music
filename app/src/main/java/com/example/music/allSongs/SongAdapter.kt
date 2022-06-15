package com.example.music.allSongs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.music.database.Song
import com.example.music.databinding.ListItemSongBinding

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */
class SongAdapter(val clickListener: SongListener): ListAdapter<Song,SongAdapter.SongViewHolder>(SongInfoDiffCallback()) {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val item = getItem(position)
        holder.songName.text = item.getTitle()
        holder.songPosition.text = (position + 1).toString()
        val timeS = (item.getDuration()/1000).toInt()
        val min = (timeS / 60)
        val sec = (timeS % 60)
        holder.songDuration.text = ("$min: $sec").toString()
        holder.bind(clickListener)
        holder.binding.song = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSongBinding.inflate(layoutInflater,parent,false)
        return SongViewHolder(binding)

    }
    class SongViewHolder(val binding: ListItemSongBinding): RecyclerView.ViewHolder(binding.root){
        val songPosition : TextView = binding.position
        val songName: TextView = binding.songName
        val songDuration: TextView = binding.duration
        fun bind(clickListener: SongListener){
            binding.clickListener = clickListener
        }

    }
}
class SongInfoDiffCallback: DiffUtil.ItemCallback<Song>(){
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return  oldItem.getAlbumId() == newItem.getAlbumId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}
class SongListener(val clickListener: (songId: Long) -> Unit){
    fun onClick(song: Song)= clickListener(song.getAlbumId())
}

