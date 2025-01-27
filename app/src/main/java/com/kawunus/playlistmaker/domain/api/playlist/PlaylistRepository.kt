package com.kawunus.playlistmaker.domain.api.playlist

import com.kawunus.playlistmaker.data.dto.PlaylistDto
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun createNewPlaylist(playlistDto: PlaylistDto)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    fun getTracks(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun deletePlaylistById(playlistId: Int)

    suspend fun updatePlaylist(playlistDto: PlaylistDto, playlist: Playlist)
}