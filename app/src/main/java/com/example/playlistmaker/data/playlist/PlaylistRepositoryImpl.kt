package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao) : PlaylistRepository {
    override fun getPlaylists(): Flow<List<PlaylistEntity>> = flow {
        val playlistList = playlistDao.getPlaylists()
        emit(playlistList)
    }

    override suspend fun createNewPlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.createNewPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.deletePlaylist(playlistEntity)
    }
}