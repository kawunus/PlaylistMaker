package com.kawunus.playlistmaker.presentation.playlist_info.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kawunus.playlistmaker.core.ui.BaseViewModel
import com.kawunus.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.kawunus.playlistmaker.domain.api.sharing.SharingInteractor
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.playlist_info.ui.model.TracksInPlaylistState
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private var playlist: Playlist,
    private val sharingInteractor: SharingInteractor
) : BaseViewModel() {
    private val tracksInPlaylistState = MutableLiveData<TracksInPlaylistState>()
    fun observeTracksInPlaylist(): LiveData<TracksInPlaylistState> = tracksInPlaylistState

    fun getData() {
        renderState(TracksInPlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor.getTracks(playlistId = playlist.id).collect { trackList ->
                processResult(trackList)
            }
        }
    }

    private fun renderState(state: TracksInPlaylistState) {
        tracksInPlaylistState.value = state
    }

    private suspend fun processResult(trackList: List<Track>) {
        val updatedPlaylist = playlistInteractor.getPlaylistById(playlistId = playlist.id)
        playlist = updatedPlaylist
        if (trackList.isEmpty()) {
            renderState(TracksInPlaylistState.Empty(playlist))
        } else renderState(TracksInPlaylistState.Content(trackList, playlist))
    }

    fun sharePlaylist(trackList: List<Track>) {
        sharingInteractor.sharePlaylist(trackList, playlist.name, playlist.description)
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(playlist, track)
            getData()
        }
    }

    private val deletedStateLiveData = MutableLiveData<Boolean>(false)
    fun observeDeleteState(): LiveData<Boolean> = deletedStateLiveData

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId = playlist.id)
            deletedStateLiveData.value = true
        }
    }
}