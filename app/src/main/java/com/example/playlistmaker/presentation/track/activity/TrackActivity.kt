package com.example.playlistmaker.presentation.track.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.track.view_model.TrackViewModel
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private val viewModel: TrackViewModel by viewModel {
        parametersOf(intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        val model = intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)!!

        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName
        binding.trackTimeTextView.text = dateFormatter.format(model.trackTimeMillis)
        Glide.with(this).load(model.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).into(binding.imageView)
        binding.trackCountryTextView.text = model.country
        binding.trackGenreTextView.text = model.primaryGenreName
        binding.trackYearTextView.text = model.releaseDate.substring(0, 4)
        binding.currentTimeTextView.text = getString(R.string.track_current_time)

        if (model.collectionName.isEmpty()) {
            binding.yearTextView.isVisible = false
            binding.trackYearTextView.isVisible = false
        } else {
            binding.trackAlbumTextView.text = model.collectionName
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observePlayerState().observe(this) { playerState ->
            when (playerState!!) {
                MediaPlayerConsts.STATE_DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                    binding.playButton.isEnabled = false
                }

                MediaPlayerConsts.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                    binding.playButton.isEnabled = true
                }

                MediaPlayerConsts.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.ic_pause)
                    binding.playButton.isEnabled = true
                }

                MediaPlayerConsts.STATE_PREPARED -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                    binding.playButton.isEnabled = true
                }
            }
        }
        viewModel.preparePlayer()
        viewModel.observeTimer().observe(this) { time ->
            binding.currentTimeTextView.text = time
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }
}

