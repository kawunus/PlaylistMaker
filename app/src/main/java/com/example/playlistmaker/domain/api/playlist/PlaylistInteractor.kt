package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun createNewPlaylist(playlistDto: PlaylistDto)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    fun getTracks(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun deletePlaylistById(playlistId: Int)
}