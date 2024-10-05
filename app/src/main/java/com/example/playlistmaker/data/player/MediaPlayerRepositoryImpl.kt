package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState: MediaPlayerConsts = MediaPlayerConsts.STATE_DEFAULT

    private lateinit var onPlayButton: () -> Unit
    private lateinit var onPauseButton: () -> Unit
    private lateinit var onSetTimer: (time: String) -> Unit

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerConsts.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerConsts.STATE_PREPARED
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerConsts.STATE_PAUSED
        onPlayButton.invoke()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerConsts.STATE_PLAYING
        mainThreadHandler.post(createUpdateTimerTask())
        onPauseButton.invoke()
    }

    override fun closePlayer() {
        if (playerState == MediaPlayerConsts.STATE_PLAYING) mediaPlayer.stop()
        mediaPlayer.reset()
        playerState = MediaPlayerConsts.STATE_DEFAULT
    }

    override fun getPlayerState(): MediaPlayerConsts {
        return playerState
    }

    override fun setResources(
        onPlayButton: () -> Unit,
        onPauseButton: () -> Unit,
        onSetTimer: (time: String) -> Unit
    ) {
        this.onPlayButton = onPlayButton
        this.onPauseButton = onPauseButton
        this.onSetTimer = onSetTimer
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val trackTime = mediaPlayer.currentPosition

                mediaPlayer.setOnCompletionListener {
                    pausePlayer()
                    mainThreadHandler.removeCallbacks(this)
                    onPauseButton.invoke()
                    onSetTimer.invoke("00:00")
                }

                if (playerState == MediaPlayerConsts.STATE_PLAYING) {
                    onSetTimer.invoke(
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                            trackTime
                        )
                    )
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    companion object {
        const val DELAY = 50L
    }
}