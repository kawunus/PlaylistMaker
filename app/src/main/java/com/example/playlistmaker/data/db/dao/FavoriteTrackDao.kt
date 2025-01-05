package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorites(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrackFromFavorites(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY added_at ASC")
    suspend fun getFavoritesTracks(): List<FavoriteTrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE id = :trackId)")
    fun isTrackInFavorites(trackId: Long): Boolean
}