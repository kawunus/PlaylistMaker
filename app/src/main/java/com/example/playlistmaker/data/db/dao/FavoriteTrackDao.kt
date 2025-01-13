package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorites(track: FavoriteTrackEntity)

    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("SELECT * FROM tracks ORDER BY added_at DESC")
    suspend fun getFavoritesTracks(): List<FavoriteTrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM tracks WHERE id = :trackId)")
    suspend fun isTrackInFavorites(trackId: Long): Boolean
}