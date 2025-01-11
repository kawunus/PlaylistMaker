package com.example.playlistmaker.domain.impl.playlist

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override fun getPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun createNewPlaylist(playlistDto: PlaylistDto) {
        playlistRepository.createNewPlaylist(playlistDto)
    }

    override suspend fun deletePlaylist(playlistEntity: PlaylistEntity) {
        playlistRepository.deletePlaylist(playlistEntity)
    }
}