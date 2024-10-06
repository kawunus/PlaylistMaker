package com.example.playlistmaker.presentation.track.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.track.view_model.TrackViewModel
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : ComponentActivity() {
    private lateinit var binding: ActivityTrackBinding
    private lateinit var viewModel: TrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        val model = intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)!!
        viewModel = ViewModelProvider(
            this,
            TrackViewModel.getViewModelFactory(model)
        )[TrackViewModel::class]
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
            binding.yearTextView.visibility = View.GONE
            binding.trackYearTextView.visibility = View.GONE
        } else {
            binding.trackAlbumTextView.text = model.collectionName
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observePlayerState().observe(this) { playerState ->
            Log.e("AAA", "Activity: $playerState")
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
    /*
        private fun preparePlayer(track: Track) = with(binding) {

            mediaPlayerInteractor.setResources(onPlayButton = { playButton.setImageResource(R.drawable.ic_pause) },
                onPauseButton = { playButton.setImageResource(R.drawable.ic_play) },
                onSetTimer = { trackTime: String -> currentTimeTextView.text = trackTime })
            mediaPlayerInteractor.preparePlayer(track = track)
        }*/


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }
}

