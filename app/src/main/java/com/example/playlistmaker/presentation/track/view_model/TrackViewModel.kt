package com.example.playlistmaker.presentation.track.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.PlayerState

class TrackViewModel(
    private val track: Track, private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val timerLiveData = MutableLiveData<String>()

    init {
        playerStateLiveData.value = PlayerState.Default()
        timerLiveData.value = "0:00"
        mediaPlayerInteractor.setLambdas(onCompletion = {
            playerStateLiveData.value = PlayerState.Prepared()
        }, onPrepared = {
            playerStateLiveData.value = PlayerState.Prepared()
        }, onSetTimer = { trackTime: String ->
            timerLiveData.value = trackTime
        })
    }

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun observeTimer(): LiveData<String> = timerLiveData

    fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(track = track)
    }

    fun pausePlayer() {
        playerStateLiveData.value = PlayerState.Paused()
        mediaPlayerInteractor.pausePlayer()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        playerStateLiveData.value = PlayerState.Playing()
    }

    fun playbackControl() {
        when (playerStateLiveData.value!!) {
            is PlayerState.Default -> {
            }

            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared -> {
                startPlayer()
            }

            is PlayerState.Paused -> {
                startPlayer()
            }
        }
    }

    fun destroyPlayer() {
        if (playerStateLiveData.value != PlayerState.Default()) mediaPlayerInteractor.closePlayer()
    }
}