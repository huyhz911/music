package com.example.music.allSongs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.database.SongInfo
import com.example.music.R

class SongAdapter:RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<SongInfo>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
       val item = data[position]
        holder.textView.text= item.getTitle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_song,parent,false)as TextView
        return TextItemViewHolder(view)

    }
}