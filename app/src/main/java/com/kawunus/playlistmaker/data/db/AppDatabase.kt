package com.kawunus.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kawunus.playlistmaker.data.db.dao.FavoriteTrackDao
import com.kawunus.playlistmaker.data.db.dao.PlaylistDao
import com.kawunus.playlistmaker.data.db.dao.PlaylistTrackDao
import com.kawunus.playlistmaker.data.db.dao.TrackDao
import com.kawunus.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.kawunus.playlistmaker.data.db.entity.PlaylistEntity
import com.kawunus.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.kawunus.playlistmaker.data.db.entity.TrackEntity


@Database(
    version = 1,
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackDao(): TrackDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}