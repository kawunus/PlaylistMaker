package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.domain.model.track.Track

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "image_name") val imageName: String?,
    @ColumnInfo(name = "count_of_tracks") val countOfTracks: Int,
    @ColumnInfo(name = "track_list") val trackList: List<Track>
)