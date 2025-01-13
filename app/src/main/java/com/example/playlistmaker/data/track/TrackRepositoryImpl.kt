package com.example.playlistmaker.data.track

import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.converter.TrackConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val trackDao: TrackDao, private val converter: TrackConverter
) : TrackRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = trackDao.getFavoritesTracks()
        emit(convertListFromTrackEntity(tracks))
    }

    override suspend fun deleteTrackFromFavorites(trackId: Long) {
        trackDao.deleteTrackById(trackId)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        trackDao.addTrackToFavorites(convertTrackToTrackEntity(track, true))
    }

    override suspend fun isTrackInFavorites(trackId: Long): Boolean {
        return trackDao.isTrackInFavorites(trackId)
    }

    private fun convertListFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }

    private fun convertTrackToTrackEntity(track: Track, isFavorite: Boolean): TrackEntity {
        return converter.map(track, System.currentTimeMillis(), isFavorite)
    }
}