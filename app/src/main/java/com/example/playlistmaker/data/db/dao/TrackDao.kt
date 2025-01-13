package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorites(track: TrackEntity)

    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("SELECT * FROM tracks WHERE is_favorite = true ORDER BY added_at DESC")
    suspend fun getFavoritesTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM tracks WHERE id = :trackId AND is_favorite = true)")
    suspend fun isTrackInFavorites(trackId: Long): Boolean
}