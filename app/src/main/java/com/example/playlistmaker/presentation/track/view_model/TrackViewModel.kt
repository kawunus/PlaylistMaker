package com.example.playlistmaker.presentation.track.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackViewModel(
    private val track: Track, private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()

    init {
        playerStateLiveData.value = PlayerState.Default()
        mediaPlayerInteractor.setLambdas(onCompletion = {
            timerJob?.cancel()
            playerStateLiveData.value = PlayerState.Prepared()
        }, onPrepared = {
            playerStateLiveData.value = PlayerState.Prepared()
        })
    }

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(track = track)
    }

    fun pausePlayer() {
        timerJob?.cancel()
        playerStateLiveData.value =
            PlayerState.Paused(mediaPlayerInteractor.getCurrentPlayerPosition())
        mediaPlayerInteractor.pausePlayer()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        playerStateLiveData.value =
            PlayerState.Playing(mediaPlayerInteractor.getCurrentPlayerPosition())
        startTimer()
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

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerStateLiveData.value is PlayerState.Playing) {
                delay(UPDATE_TIMER_DEBOUNCE)
                playerStateLiveData.postValue(PlayerState.Playing(mediaPlayerInteractor.getCurrentPlayerPosition()))
            }
        }
    }

    companion object {
        private const val UPDATE_TIMER_DEBOUNCE = 300L
    }
}