package com.kawunus.playlistmaker.data.dto

data class TrackDto(
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
)