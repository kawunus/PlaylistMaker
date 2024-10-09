package com.example.playlistmaker.presentation.track.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts
import com.example.playlistmaker.utils.creator.Creator

class TrackViewModel(private val track: Track) : ViewModel() {

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TrackViewModel(track)
                }
            }
    }

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(
        onCompletion = {
            playerStateLiveData.value = MediaPlayerConsts.STATE_PREPARED
        },
        onPrepared = {
            playerStateLiveData.value = MediaPlayerConsts.STATE_PREPARED
        },
        onSetTimer = { trackTime: String ->
            timerLiveData.value = trackTime
        }
    )

    private val playerStateLiveData = MutableLiveData<MediaPlayerConsts>()
    private val timerLiveData = MutableLiveData<String>()

    init {
        playerStateLiveData.value = MediaPlayerConsts.STATE_DEFAULT
        timerLiveData.value = "0:00"
    }

    fun observePlayerState(): LiveData<MediaPlayerConsts> = playerStateLiveData

    fun observeTimer(): LiveData<String> = timerLiveData

    fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(track = track)
    }

    fun pausePlayer() {
        playerStateLiveData.value = MediaPlayerConsts.STATE_PAUSED
        mediaPlayerInteractor.pausePlayer()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        playerStateLiveData.value = MediaPlayerConsts.STATE_PLAYING
    }

    fun playbackControl() {
        when (playerStateLiveData.value!!) {
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

    fun destroyPlayer() {
        if (playerStateLiveData.value != MediaPlayerConsts.STATE_DEFAULT) mediaPlayerInteractor.closePlayer()
    }
}