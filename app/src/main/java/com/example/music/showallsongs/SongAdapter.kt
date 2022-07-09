package com.example.music.showallsongs

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

/**
 * Bkav HuyNgQe: gán dữ liệu cho item trong recyclerView
 */
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val item = getItem(position)
        holder.songName.text = item.songName
        holder.songPosition.text = (position + 1).toString()
        item.duration.let {
            val timeS = (it?.div(1000))?.toInt()
            val min = (timeS?.div(60))
            val sec = (timeS?.rem(60))
            holder.songDuration.text = ("$min: $sec").toString()
        }

        holder.bind(clickListener)
        holder.binding.song = item
    }
  /**
   * Bkav HuyNgQe: tạo ViewHolder
   */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSongBinding.inflate(layoutInflater,parent,false)
        return SongViewHolder(binding)

    }
    /**
     * Bkav HuyNgQe: khai báo ui trong item của recyclerView
     */
    class SongViewHolder(val binding: ListItemSongBinding): RecyclerView.ViewHolder(binding.root){
        val songPosition : TextView = binding.position
        val songName: TextView = binding.songName
        val songDuration: TextView = binding.duration
        fun bind(clickListener: SongListener){
            binding.clickListener = clickListener
        }
    }
}
/**
 * Bkav HuyNgQe:kiểm tra item trùng trong recyclerView
 */
class SongInfoDiffCallback: DiffUtil.ItemCallback<Song>(){
    /*Bkav HuyNgQe: kiểm tra các item có id giống nhau */
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return  oldItem.songID == newItem.songID
    }

 /*Bkav HuyNgQe: kiểm tra item giống nhau */
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}
/**
 * Bkav HuyNgQe:bắt sự kiện khi click vào item
 */
class SongListener(val clickListener: (songId: Song) -> Unit){
    fun onClick(song: Song)= clickListener(song)
}

