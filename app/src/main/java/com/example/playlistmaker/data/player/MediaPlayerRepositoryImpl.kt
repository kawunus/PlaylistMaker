package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
) : MediaPlayerRepository {

    private lateinit var onPrepared: () -> Unit
    private lateinit var onCompletion: () -> Unit

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPrepared.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCompletion.invoke()
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun closePlayer() {
        if (playerState == STATE_PLAYING) mediaPlayer.stop()
        mediaPlayer.reset()
        playerState = STATE_DEFAULT
    }

    override fun setLambdas(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        this.onPrepared = onPrepared
        this.onCompletion = onCompletion
    }

    override fun getCurrentPlayerPosition(): String {
        val currentPosition = mediaPlayer.currentPosition
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}