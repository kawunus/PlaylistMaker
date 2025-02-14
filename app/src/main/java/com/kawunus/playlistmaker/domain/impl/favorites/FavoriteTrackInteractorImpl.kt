package com.kawunus.playlistmaker.domain.impl.favorites

import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackInteractor
import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackRepository
import com.kawunus.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(private val repository: FavoriteTrackRepository) :
    FavoriteTrackInteractor {
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