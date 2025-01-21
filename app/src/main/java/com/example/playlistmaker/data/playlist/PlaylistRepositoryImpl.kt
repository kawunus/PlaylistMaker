package com.example.playlistmaker.data.playlist

import android.util.Log
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.dto.PlaylistDto
import com.example.playlistmaker.data.file_manager.FileManager
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.converter.PlaylistConverter
import com.example.playlistmaker.utils.converter.TrackConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val trackDao: TrackDao,
    private val fileManager: FileManager,
    private val playlistConverter: PlaylistConverter,
    private val trackConverter: TrackConverter
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
            imageName = fileName
        )

        playlistDao.createNewPlaylist(playlist)
        Log.e("DATABASE_PLAYLISTS", "${playlistDao.getPlaylists()}")
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }

    private fun convertPlaylistListFromEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity ->
            playlistConverter.map(playlistEntity)
        }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        if (playlistTrackDao.getPlaylistTrackById(playlist.id, track.trackId).isEmpty()) {
            val trackEntity =
                trackConverter.map(track = track, addedAt = System.currentTimeMillis())
            trackDao.createTrack(trackEntity)
            val playlistTrack =
                PlaylistTrackEntity(playlistId = playlist.id, trackId = track.trackId)
            playlistTrackDao.createPlaylistTrack(playlistTrack)
            val newSize = playlistTrackDao.getTracksByPlaylistId(playlistId = playlist.id).size
            val newPlaylistEntity = playlistConverter.map(playlist.copy(countOfTracks = newSize))
            playlistDao.updatePlaylist(newPlaylistEntity)
            return true
        } else {
            return false
        }
    }

    override fun getTracks(playlistId: Int): Flow<List<Track>> = flow {
        val relationList = playlistTrackDao.getTracksByPlaylistId(playlistId)
        val trackList = relationList
            .mapNotNull { relation -> trackDao.getTrackById(trackId = relation.trackId) }
            .sortedByDescending { track -> track.addedAt }

        emit(convertTrackListFromEntity(trackList))
    }

    private fun convertTrackListFromEntity(trackList: List<TrackEntity>): List<Track> {
        return trackList.map { track ->
            trackConverter.map(track)
        }
    }
}