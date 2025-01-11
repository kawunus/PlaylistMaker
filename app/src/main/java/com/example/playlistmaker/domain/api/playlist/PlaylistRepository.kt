package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun createNewPlaylist(playlistEntity: PlaylistEntity)

    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)
}