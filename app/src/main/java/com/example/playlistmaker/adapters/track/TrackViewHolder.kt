package com.example.playlistmaker.adapters.track

import android.content.Context
import android.content.Intent
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
import com.example.playlistmaker.ui.activities.TrackActivity
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
            val intent = Intent(context, TrackActivity::class.java)
            intent.putExtra("trackName", model.trackName)
            intent.putExtra("artistName", model.artistName)
            intent.putExtra("trackTime", binding.trackTimeTextView.text)
            intent.putExtra(
                "artworkUrl",
                model.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            )
            intent.putExtra("trackCountry", model.country)
            intent.putExtra("trackGenre", model.primaryGenreName)
            intent.putExtra("trackYear", model.releaseDate.substring(0,4))
            intent.putExtra("trackAlbum", model.collectionName)
            context.startActivity(intent)
        }
    }
}