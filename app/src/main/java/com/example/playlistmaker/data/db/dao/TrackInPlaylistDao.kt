package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist WHERE id IN (:trackIds)")
    fun getAllTracksByPlaylistIds(trackIds: List<Int>): Flow<List<TrackInPlaylistEntity>>

    @Query("SELECT id FROM track_in_playlist")
    suspend fun getAllTracksIds(): List<Int>

    @Query("DELETE FROM track_in_playlist WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Int)
}