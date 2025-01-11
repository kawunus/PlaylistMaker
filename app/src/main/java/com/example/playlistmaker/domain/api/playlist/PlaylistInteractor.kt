package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.dto.PlaylistDto
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun createNewPlaylist(playlistDto: PlaylistDto)

    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)
}