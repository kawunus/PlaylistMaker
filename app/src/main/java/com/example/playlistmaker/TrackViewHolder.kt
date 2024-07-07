package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {

    private val artworkImageView: ImageView = trackView.findViewById(R.id.artworkImageView)
    private val trackNameTextView: TextView = trackView.findViewById(R.id.trackNameTextView)
    private val artistNameTextView: TextView = trackView.findViewById(R.id.artistNameTextView)
    private val trackTimeTextView: TextView = trackView.findViewById(R.id.trackTimeTextView)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(artworkImageView)
        trackNameTextView.text = getCurrentStrind(model.trackName)
        artistNameTextView.text = getCurrentStrind(model.artistName)
        trackTimeTextView.text = model.trackTime
    }

    private fun getCurrentStrind(string: String): String {
        return if (string.length <= 33) string
        else string.substring(0, 29) + "..."
    }
}