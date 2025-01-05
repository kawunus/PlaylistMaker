package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "track_name")
    val trackName: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "track_time_millis")
    val trackTimeMillis: Int,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl100: String,
    @ColumnInfo(name = "track_id_on_itunes")
    val trackIdOnItunes: Long,
    val country: String,
    @ColumnInfo(name = "primary_genre_name")
    val primaryGenreName: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String
)