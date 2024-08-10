package com.example.playlistmaker.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.trackNameTextView.text = intent.getStringExtra("trackName")
        binding.artistNameTextView.text = intent.getStringExtra("artistName")
        binding.trackTimeTextView.text = intent.getStringExtra("trackTime")
        Glide.with(this).load(intent.getStringExtra("artworkUrl")).placeholder(R.drawable.placeholder).into(binding.imageView)
        binding.trackCountryTextView.text = intent.getStringExtra("trackCountry")
        binding.trackGenreTextView.text = intent.getStringExtra("trackGenre")
        binding.trackYearTextView.text = intent.getStringExtra("trackYear")
        binding.currentTimeTextView.text = getString(R.string.track_current_time)

        if (intent.getStringExtra("trackAlbum").toString().isEmpty())
        {
            binding.yearTextView.visibility = View.GONE
            binding.trackYearTextView.visibility = View.GONE
        }
        else {
            binding.trackAlbumTextView.text = intent.getStringExtra("trackAlbum")
        }
    }
}