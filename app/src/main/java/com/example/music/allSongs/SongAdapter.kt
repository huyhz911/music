package com.example.music.allSongs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.music.database.SongInfo
import com.example.music.R

class SongAdapter: ListAdapter<SongInfo,SongAdapter.SongViewHolder>(SongInfoDiffCallback()) {
//    var data = ArrayList<SongInfo>()
//    set(value) {
//        field = value
//        notifyDataSetChanged()
//    }
//    override fun getItemCount() = data.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val item = getItem(position)
        holder.songName.text = item.getTitle()
        holder.songPosition.text = (position + 1).toString()
        holder.songDuration.text = item.getDuration().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_song,parent,false) as ConstraintLayout
        return SongViewHolder(view)

    }
    class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val songPosition : TextView = itemView.findViewById(R.id.position)
        val songName: TextView = itemView.findViewById(R.id.songName)
        val songDuration: TextView = itemView.findViewById(R.id.duration)

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

