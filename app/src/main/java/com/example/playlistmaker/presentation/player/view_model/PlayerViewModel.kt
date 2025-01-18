package com.example.playlistmaker.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.favorite.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.player.ui.model.PlayerPlaylistState
import com.example.playlistmaker.presentation.player.ui.model.PlayerState
import com.example.playlistmaker.utils.single_live_event.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private val isFavoriteLiveData = MutableLiveData<Boolean>()

    private val playerPlaylistState = MutableLiveData<PlayerPlaylistState>()

    init {
        playerStateLiveData.value = PlayerState.Default()
        mediaPlayerInteractor.setLambdas(onCompletion = {
            timerJob?.cancel()
            playerStateLiveData.value = PlayerState.Prepared()
        }, onPrepared = {
            playerStateLiveData.value = PlayerState.Prepared()
        })

        viewModelScope.launch {
            isFavoriteLiveData.value =
                favoriteTrackInteractor.isTrackInFavorites(trackId = track.trackId)
        }
    }

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun observeIsFavoriteState(): LiveData<Boolean> = isFavoriteLiveData

    fun observePlayerPlaylistState(): LiveData<PlayerPlaylistState> = playerPlaylistState

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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlistList ->
                processResult(playlistList)
            }
        }
    }

    private fun deleteTrackFromFavorites() {
        viewModelScope.launch {
            favoriteTrackInteractor.deleteTrackFromFavorites(trackId = track.trackId)
            isFavoriteLiveData.value =
                favoriteTrackInteractor.isTrackInFavorites(trackId = track.trackId)
        }
    }

    fun likeButtonControl() {
        if (isFavoriteLiveData.value == true) {
            deleteTrackFromFavorites()
        } else {
            addTrackToFavorites()
        }
    }

    private fun addTrackToFavorites() {
        viewModelScope.launch {
            favoriteTrackInteractor.addTrackToFavorites(track)
            isFavoriteLiveData.value =
                favoriteTrackInteractor.isTrackInFavorites(trackId = track.trackId)
        }
    }

    private fun processResult(playlistList: List<Playlist>) {
        if (playlistList.isEmpty()) {
            renderPlaylistsState(PlayerPlaylistState.Empty)
        } else renderPlaylistsState(PlayerPlaylistState.Content(playlistList))
    }

    private fun renderPlaylistsState(state: PlayerPlaylistState) {
        playerPlaylistState.value = state
    }

    companion object {
        private const val UPDATE_TIMER_DEBOUNCE = 300L
    }

    private val showToast = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            val isAdded = playlistInteractor.addTrackToPlaylist(track = track, playlist = playlist)

            if (isAdded) {
                showToast.postValue("Добавлено в плейлист ${playlist.name}")
            } else {
                showToast.postValue("Трек уже добавлен в плейлист ${playlist.name}")
            }

            getPlaylists()
        }
    }
}