package com.kawunus.playlistmaker.data.favorites

import com.kawunus.playlistmaker.data.db.dao.FavoriteTrackDao
import com.kawunus.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.kawunus.playlistmaker.domain.api.favorite.FavoriteTrackRepository
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.utils.converter.FavoriteTrackConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val favoriteTrackDao: FavoriteTrackDao, private val converter: FavoriteTrackConverter
) : FavoriteTrackRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = favoriteTrackDao.getFavoritesTracks()
        emit(convertListFromTrackEntity(tracks))
    }

    override suspend fun deleteTrackFromFavorites(trackId: Long) {
        favoriteTrackDao.deleteTrackById(trackId)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        favoriteTrackDao.addTrackToFavorites(convertTrackToTrackEntity(track))
    }

    override suspend fun isTrackInFavorites(trackId: Long): Boolean {
        return favoriteTrackDao.isTrackInFavorites(trackId)
    }

    private fun convertListFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }

    private fun convertTrackToTrackEntity(track: Track): FavoriteTrackEntity {
        return converter.map(track, System.currentTimeMillis())
    }
}