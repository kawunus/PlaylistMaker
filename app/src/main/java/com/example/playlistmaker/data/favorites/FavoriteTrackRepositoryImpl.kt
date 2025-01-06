package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.domain.api.favorites.FavoriteTrackRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.converter.TrackConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase, private val converter: TrackConverter
) : FavoriteTrackRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoriteTrackDao().getFavoritesTracks()
        emit(convertListFromTrackEntity(tracks))
    }

    override suspend fun deleteTrackFromFavorites(trackId: Long) {
        appDatabase.favoriteTrackDao().deleteTrackById(trackId)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.favoriteTrackDao().addTrackToFavorites(convertTrackToTrackEntity(track))
    }

    override suspend fun isTrackInFavorites(trackId: Long): Boolean {
        return appDatabase.favoriteTrackDao().isTrackInFavorites(trackId)
    }

    private fun convertListFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }

    private fun convertTrackToTrackEntity(track: Track): FavoriteTrackEntity {
        return converter.map(track, System.currentTimeMillis())
    }
}