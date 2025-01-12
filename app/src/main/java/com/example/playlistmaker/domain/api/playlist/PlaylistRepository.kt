package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.domain.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun createNewPlaylist(playlistDto: PlaylistDto)

    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)
}