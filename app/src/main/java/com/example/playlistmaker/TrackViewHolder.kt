package com.example.playlistmaker

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackViewBinding

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val binding: TrackViewBinding = TrackViewBinding.bind(trackView)
    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(binding.artworkImageView)
        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = getCurrentStrind(model.artistName)
        binding.trackTimeTextView.text = model.trackTime
    }

    private fun getCurrentStrind(string: String): String {
        return if (string.length <= 43) string
        else string.substring(0, 39) + "..."
    }
}