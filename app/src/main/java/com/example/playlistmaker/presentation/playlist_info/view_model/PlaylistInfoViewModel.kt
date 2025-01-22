package com.example.playlistmaker.presentation.playlist_info.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.playlist_info.ui.model.TracksInPlaylistState
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private var playlist: Playlist,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
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

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId = playlist.id)
        }
    }
}