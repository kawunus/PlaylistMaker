package com.example.playlistmaker.domain.api.favorites

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {

    fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun deleteTrackFromFavorites(trackId: Long)

    suspend fun addTrackToFavorites(track: Track)

    suspend fun isTrackInFavorites(trackId: Long): Boolean
}