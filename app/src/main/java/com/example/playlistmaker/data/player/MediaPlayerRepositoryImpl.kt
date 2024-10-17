package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
) : MediaPlayerRepository {

    private lateinit var onPrepared: () -> Unit
    private lateinit var onCompletion: () -> Unit
    private lateinit var onSetTimer: (time: String) -> Unit

    private var playerState: MediaPlayerConsts = MediaPlayerConsts.STATE_DEFAULT

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerConsts.STATE_PREPARED
            onPrepared.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerConsts.STATE_PREPARED
            onCompletion.invoke()
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerConsts.STATE_PAUSED
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerConsts.STATE_PLAYING
        mainThreadHandler.post(createUpdateTimerTask())
    }

    override fun closePlayer() {
        if (playerState == MediaPlayerConsts.STATE_PLAYING) mediaPlayer.stop()
        mediaPlayer.reset()
        playerState = MediaPlayerConsts.STATE_DEFAULT
    }

    override fun setLambdas(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onSetTimer: (time: String) -> Unit
    ) {
        this.onPrepared = onPrepared
        this.onCompletion = onCompletion
        this.onSetTimer = onSetTimer
    }


    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val trackTime = mediaPlayer.currentPosition

                mediaPlayer.setOnCompletionListener {
                    pausePlayer()
                    mainThreadHandler.removeCallbacks(this)
                    onSetTimer.invoke("00:00")
                    onCompletion.invoke()
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