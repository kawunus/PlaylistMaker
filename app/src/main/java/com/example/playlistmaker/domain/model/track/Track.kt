package com.example.playlistmaker.domain.model.track

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Long,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val collectionName: String,
    val previewUrl: String
) : Parcelable