package com.example.playlistmaker.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.data.track.Track
import com.example.playlistmaker.databinding.ActivityTrackBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        val model = intent.getParcelableExtra<Track>("track")

        binding.trackNameTextView.text = model?.trackName
        binding.artistNameTextView.text = model?.artistName
        binding.trackTimeTextView.text = dateFormatter.format(model?.trackTime)
        Glide.with(this).load(model?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).into(binding.imageView)
        binding.trackCountryTextView.text = model?.country
        binding.trackGenreTextView.text = model?.primaryGenreName
        binding.trackYearTextView.text = model?.releaseDate?.substring(0, 4)
        binding.currentTimeTextView.text = getString(R.string.track_current_time)

        if (model?.collectionName?.isEmpty() == true) {
            binding.yearTextView.visibility = View.GONE
            binding.trackYearTextView.visibility = View.GONE
        } else {
            binding.trackAlbumTextView.text = model?.collectionName
        }
    }
}