package com.example.playlistmaker.adapters.track

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.track.Track
import com.example.playlistmaker.data.track.prefs.PrefKeys
import com.example.playlistmaker.data.track.prefs.historyprefs.HistoryPrefs
import com.example.playlistmaker.databinding.TrackViewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(trackView: View, private val context: Context) :
    RecyclerView.ViewHolder(trackView) {
    private val binding: TrackViewBinding = TrackViewBinding.bind(trackView)
    fun bind(model: Track) {
        Glide.with(itemView).load(model.artworkUrl100).transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder).into(binding.artworkImageView)
        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName

        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.trackTimeTextView.text = dateFormatter.format(model.trackTime)
        itemView.setOnClickListener {
            val historyPrefs = HistoryPrefs(
                context.getSharedPreferences(
                    PrefKeys.PREFS, MODE_PRIVATE
                )
            )
            historyPrefs.addToHistoryList(track = model)
        }
    }
}