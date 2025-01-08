package com.example.playlistmaker.presentation.search.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.model.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val binding: TrackViewBinding = TrackViewBinding.bind(trackView)
    fun bind(model: Track) {
        Glide.with(itemView).load(model.artworkUrl100).transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder).into(binding.artworkImageView)
        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName

        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.trackTimeTextView.text = dateFormatter.format(model.trackTimeMillis)
    }
}