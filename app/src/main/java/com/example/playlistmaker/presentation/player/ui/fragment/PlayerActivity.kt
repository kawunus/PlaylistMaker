package com.example.playlistmaker.presentation.player.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.player.ui.model.PlayerState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.utils.consts.IntentConsts
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val model = intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)!!

        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName
        binding.trackTimeTextView.text = dateFormatter.format(model.trackTimeMillis)
        Glide.with(this).load(model.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).transform(
            RoundedCorners(8)
        ).placeholder(R.drawable.placeholder).into(binding.imageView)
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

        viewModel.observeIsFavoriteState().observe(this) { isFavorite ->
            if (isFavorite) {
                binding.likeButton.setImageResource(R.drawable.ic_is_liked)
                binding.likeButton.setOnClickListener {
                    viewModel.deleteTrackFromFavorites()
                }
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like)
                binding.likeButton.setOnClickListener {
                    viewModel.addTrackToFavorites()
                }
            }
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observePlayerState().observe(this) { playerState ->
            when (playerState!!) {
                is PlayerState.Default -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.ic_pause)
                }

                is PlayerState.Prepared -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }
            }
            Log.e("PLAYER_STATE", "${playerState}")
            binding.currentTimeTextView.text = playerState.progress
            binding.playButton.isEnabled = playerState.isPlayButtonEnabled
        }
        viewModel.preparePlayer()
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

