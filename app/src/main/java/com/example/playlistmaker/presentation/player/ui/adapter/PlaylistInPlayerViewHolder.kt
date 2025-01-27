package com.example.playlistmaker.presentation.player.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistInPlayerBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.utils.converter.WordConverter

class PlaylistInPlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemPlaylistInPlayerBinding = ItemPlaylistInPlayerBinding.bind(view)

    fun bind(model: Playlist) {
        binding.coverImageView.load(model.imageUrl) {
            placeholder(R.drawable.playlist_placeholder)
            error(R.drawable.playlist_placeholder)
        }
        binding.nameTextView.text = model.name
        val countOfTracks =
            "${model.countOfTracks} ${WordConverter.getTrackWordForm(model.countOfTracks)}"
        binding.countTextView.text = countOfTracks
    }
}