package com.example.playlistmaker.domain.impl.favorites

import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class TrackInteractorImpl(private val repository: TrackRepository) :
    TrackInteractor {
    override fun getFavoritesTracks(): Flow<List<Track>> {
        return repository.getFavoritesTracks()
    }

    override suspend fun deleteTrackFromFavorites(trackId: Long) {
        repository.deleteTrackFromFavorites(trackId)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun isTrackInFavorites(trackId: Long): Boolean {
        return repository.isTrackInFavorites(trackId)
    }
}