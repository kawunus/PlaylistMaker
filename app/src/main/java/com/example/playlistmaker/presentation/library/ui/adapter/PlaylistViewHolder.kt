package com.example.playlistmaker.presentation.library.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemInMediatekaBinding
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: PlaylistItemInMediatekaBinding = PlaylistItemInMediatekaBinding.bind(view)

    fun bind(model: Playlist) {
        Glide.with(itemView).load(model.imageUrl).transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder).into(binding.coverImageView)
        binding.playlistNameTextView.text = model.name
        binding.trackCountTextView.text = model.countOfTracks.toString()
    }
}