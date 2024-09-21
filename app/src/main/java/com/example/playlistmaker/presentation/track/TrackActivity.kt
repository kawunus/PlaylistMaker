package com.example.playlistmaker.presentation.track

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import com.example.playlistmaker.utils.creator.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding

    private var creator = Creator()
    private val mediaPlayerInteractor =
        creator.provideMediaPlayerInteractor(mediaPlayer = MediaPlayer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        val model = intent.getParcelableExtra<Track>(IntentConsts.TRACK.name)

        binding.trackNameTextView.text = model?.trackName
        binding.artistNameTextView.text = model?.artistName
        binding.trackTimeTextView.text = dateFormatter.format(model?.trackTimeMillis)
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

        preparePlayer(model!!)
        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer(track: Track) = with(binding) {
        mediaPlayerInteractor.setResources(onPlayButton = { playButton.setImageResource(R.drawable.ic_pause) },
            onPauseButton = { playButton.setImageResource(R.drawable.ic_play) },
            onSetTimer = { trackTime: String -> currentTimeTextView.text = trackTime })
        mediaPlayerInteractor.preparePlayer(track = track)/*onPrepared = {
                playButton.isEnabled = true
                binding.currentTimeTextView.text = getString(R.string.track_current_time)
            },
            onCompletion = {
                mainThreadHandler.removeCallbacks(timerThread!!)
                binding.currentTimeTextView.text = getString(R.string.track_current_time)
                binding.playButton.setImageResource(R.drawable.ic_play)
            }*/
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        binding.playButton.setImageResource(R.drawable.ic_pause)

    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        binding.playButton.setImageResource(R.drawable.ic_play)
    }

    private fun playbackControl() {
        when (mediaPlayerInteractor.getPlayerState()) {

            MediaPlayerConsts.STATE_DEFAULT -> {
            }

            MediaPlayerConsts.STATE_PLAYING -> {
                pausePlayer()
            }

            MediaPlayerConsts.STATE_PREPARED, MediaPlayerConsts.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayerInteractor.getPlayerState() == MediaPlayerConsts.STATE_PLAYING) {
            pausePlayer()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayerInteractor.getPlayerState() != MediaPlayerConsts.STATE_DEFAULT) mediaPlayerInteractor.closePlayer()

    }
}

