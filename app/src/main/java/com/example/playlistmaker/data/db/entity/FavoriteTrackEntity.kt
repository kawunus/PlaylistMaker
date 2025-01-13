package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class FavoriteTrackEntity(
    @PrimaryKey @ColumnInfo(name = "id") val trackId: Long,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "track_time_millis") val trackTimeMillis: Int,
    @ColumnInfo(name = "artwork_url") val artworkUrl100: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "primary_genre_name") val primaryGenreName: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "collection_name") val collectionName: String,
    @ColumnInfo(name = "preview_url") val previewUrl: String,
    @ColumnInfo(name = "added_at") val addedAt: Long
)