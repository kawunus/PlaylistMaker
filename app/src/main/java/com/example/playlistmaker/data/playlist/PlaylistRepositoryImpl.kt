package com.example.playlistmaker.data.playlist

import android.util.Log
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.data.file_manager.FileManager
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.utils.converter.PlaylistConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val fileManager: FileManager,
    private val converter: PlaylistConverter
) : PlaylistRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlistList = playlistDao.getPlaylists()
        emit(convertPlaylistListFromEntity(playlistList))
    }

    override suspend fun createNewPlaylist(playlistDto: PlaylistDto) {

        var filePath: String? = null

        val fileName = "${playlistDto.name}_${UUID.randomUUID()}.jpg"
        if (playlistDto.imageUrl != null) {
            filePath = fileManager.saveCoverToPrivateStorage(playlistDto.imageUrl, fileName)
        }

        val playlist = PlaylistEntity(
            name = playlistDto.name,
            description = playlistDto.description,
            countOfTracks = 0,
            imageUrl = filePath,
            imageName = fileName,
            trackIds = emptyList()
        )

        playlistDao.createNewPlaylist(playlist)
        Log.e("DATABASE_PLAYLISTS", "${playlistDao.getPlaylists()}")
    }

    override suspend fun deletePlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.deletePlaylist(playlistEntity)
    }

    private fun convertPlaylistListFromEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity ->
            converter.map(playlistEntity)
        }
    }
}