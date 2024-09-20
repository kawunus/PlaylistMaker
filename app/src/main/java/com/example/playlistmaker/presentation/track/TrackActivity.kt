package com.example.playlistmaker.presentation.track

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.IntentConsts
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.models.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private var timerThread: Runnable? = null
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        val model = intent.getParcelableExtra<Track>(IntentConsts.TRACK)

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
        preparePlayer(model!!.previewUrl)
        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer(url: String) = with(binding) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
            binding.currentTimeTextView.text = getString(R.string.track_current_time)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(timerThread!!)
            binding.currentTimeTextView.text = getString(R.string.track_current_time)
            binding.playButton.setImageResource(R.drawable.ic_play)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        binding.playButton.setImageResource(R.drawable.ic_pause)
        timerThread?.let {
            mainThreadHandler.removeCallbacks(it)
        }
        timerThread = createUpdateTimerTask()
        timerThread!!.run()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        binding.playButton.setImageResource(R.drawable.ic_play)
        mainThreadHandler.removeCallbacks(timerThread!!)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING) {
            pausePlayer()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (playerState != STATE_DEFAULT)
            mediaPlayer.release()
        timerThread?.let {
            mainThreadHandler.removeCallbacks(it)
        }

    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val currTime =
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                Log.e("TIMER", mediaPlayer.currentPosition.toString())
                binding.currentTimeTextView.text = currTime
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 50L
    }
}
