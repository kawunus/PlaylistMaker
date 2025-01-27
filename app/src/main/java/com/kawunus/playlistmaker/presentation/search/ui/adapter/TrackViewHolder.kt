package com.kawunus.playlistmaker.presentation.search.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.databinding.ItemTrackBinding
import com.kawunus.playlistmaker.domain.model.track.Track
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