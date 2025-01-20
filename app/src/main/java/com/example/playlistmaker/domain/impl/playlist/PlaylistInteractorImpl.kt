package com.example.playlistmaker.domain.impl.playlist

import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun createNewPlaylist(playlistDto: PlaylistDto) {
        playlistRepository.createNewPlaylist(playlistDto)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return playlistRepository.addTrackToPlaylist(playlist, track)
    }


}