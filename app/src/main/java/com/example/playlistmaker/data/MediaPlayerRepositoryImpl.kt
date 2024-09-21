package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState: MediaPlayerConsts = MediaPlayerConsts.STATE_DEFAULT

    private val currentTimePlayer = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(mediaPlayer.currentPosition)

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerConsts.STATE_PREPARED
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerConsts.STATE_PAUSED
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerConsts.STATE_PLAYING
    }

    override fun closePlayer() {
        if (playerState == MediaPlayerConsts.STATE_PLAYING) mediaPlayer.stop()
        mediaPlayer.reset()
        playerState = MediaPlayerConsts.STATE_DEFAULT
    }

    override fun getPlayerState(): MediaPlayerConsts {
        return playerState
    }
}