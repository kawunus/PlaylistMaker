package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun getPlaylistTrackById(playlistId: Int, trackId: Long): List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_tracks WHERE playlist_id = :playlistId")
    suspend fun getTracksByPlaylistId(playlistId: Int): List<PlaylistTrackEntity>

}