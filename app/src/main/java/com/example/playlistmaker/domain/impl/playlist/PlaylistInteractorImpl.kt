package com.example.playlistmaker.domain.impl.playlist

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistRepository {
    override fun getPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun createNewPlaylist(playlistEntity: PlaylistEntity) {
        playlistRepository.createNewPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistEntity: PlaylistEntity) {
        playlistRepository.deletePlaylist(playlistEntity)
    }
}