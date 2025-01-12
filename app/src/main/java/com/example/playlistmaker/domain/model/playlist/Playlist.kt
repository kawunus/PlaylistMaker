package com.example.playlistmaker.domain.model.playlist

data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val imageName: String?,
    val countOfTracks: Int,
)