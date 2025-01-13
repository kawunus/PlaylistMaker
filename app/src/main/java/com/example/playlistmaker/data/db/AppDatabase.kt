package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.utils.converter.TrackListConverter


@Database(
    version = 1, entities = [FavoriteTrackEntity::class, PlaylistEntity::class]
)
@TypeConverters(TrackListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    abstract fun playlistDao(): PlaylistDao
}