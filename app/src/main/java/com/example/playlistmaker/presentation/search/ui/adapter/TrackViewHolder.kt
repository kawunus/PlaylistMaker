package com.example.playlistmaker.presentation.search.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.domain.model.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val binding: ItemTrackBinding = ItemTrackBinding.bind(trackView)
    fun bind(model: Track) {
        binding.artworkImageView.load(model.artworkUrl100) {
            placeholder(R.drawable.track_placeholder)
            error(R.drawable.track_placeholder)
            transformations(RoundedCornersTransformation(2f))
        }
        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName

        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.trackTimeTextView.text = dateFormatter.format(model.trackTimeMillis)
    }
}