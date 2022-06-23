package com.example.music.allSongs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.music.ActivityMusic
import com.example.music.MyApplication
import com.example.music.R
import com.example.music.database.Song
import com.example.music.database.SongRepository
import com.example.music.databinding.AllSongsFragmentBinding
import com.example.music.mediaPlayService.MediaPlaybackService
import java.io.Serializable

/**
 * Created by Bkav HuyNgQe on 07/06/2022.
 */

class  AllSongsFragment: Fragment() {

     lateinit var songPra :Song
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = DataBindingUtil.inflate<AllSongsFragmentBinding>(inflater,
            R.layout.all_songs_fragment,
            container,
            false)
        val mediaPlaybackService = MediaPlaybackService()
        val songRepository = SongRepository()
        binding.lifecycleOwner = this
        // su ly su kien khi click vao song item
        val adapter = SongAdapter(SongListener { song ->
            // start service
            val intent = Intent(activity, MediaPlaybackService::class.java)
            val bundle= Bundle()
            val picture = GetSongPicture(song)
            bundle.putSerializable("pictureSong", picture)
            intent.putExtras(bundle)
            activity?.startService(intent)


            mediaPlaybackService.playMusic(song)
            binding.imageAlbum.setImageBitmap(songRepository.getCoverPicture(song))
            binding.textSongName.text = songRepository.getSongName(song)
            binding.textAuthor.text = song.artists
            binding.songPopUp.visibility = View.VISIBLE
            binding.togglePlayPause.isChecked = true
            songPra = song
        })
        binding.listSong.adapter = adapter
//        allSongViewModel.listSong.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//            }
//        })

        (activity as ActivityMusic).listSong.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        // chuyen doi so sang cho media play fragment
        binding.relativeLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(AllSongsFragmentDirections.actionAllSongsFragmentToMediaPlaybackFragment2(songPra))
        }
        binding.togglePlayPause.setOnClickListener { if (binding.togglePlayPause.isChecked){
              mediaPlaybackService.resumeMusic()
        }else{
            mediaPlaybackService.pauseMusic()
        }
        }

//        (activity as ActivityMusic).mService
        return  binding.root
    }


}
/**
 * Bkav HuyNgQe:lay anh bia ra de chuyen sang thong bao
 */
class  GetSongPicture(song: Song):Serializable{
    @Transient // todo
    var art: Bitmap = BitmapFactory.decodeResource(MyApplication.getContext().resources , R.drawable.bg_default_album_art)
    private var song: Song = song
    fun getPicture(): Bitmap{
                    val uri = Uri.parse(song.data)
                    val mmr = MediaMetadataRetriever()
                    val bfo = BitmapFactory.Options()
                    mmr.setDataSource(MyApplication.getContext(), uri)
                    val rawArt: ByteArray? = mmr.embeddedPicture
                    if (null != rawArt){ art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)}
        return art
    }


}
