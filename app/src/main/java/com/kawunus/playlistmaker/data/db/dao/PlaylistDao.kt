package com.kawunus.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kawunus.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY name ASC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists WHERE id = :playlistId LIMIT 1")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity
}
