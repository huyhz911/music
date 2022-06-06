package com.example.music.allSongs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.database.SongInfo
import com.example.music.R

class SongAdapter:RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private var data = listOf<SongInfo>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val item = data[position]
        holder.songName.text = item.getTitle()
        holder.songPosition.text = (position + 1).toString()
        holder.songDuration.text = item.getDuration().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_song,parent,false) as TextView
        return SongViewHolder(view)

    }
    class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val songPosition : TextView = itemView.findViewById(R.id.position)
        val songName: TextView = itemView.findViewById(R.id.songName)
        val songDuration: TextView = itemView.findViewById(R.id.duration)

    }
}

