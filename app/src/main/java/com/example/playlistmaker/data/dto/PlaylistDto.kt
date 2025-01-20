package com.example.playlistmaker.data.dto

import android.net.Uri

data class PlaylistDto(
    val name: String,
    val description: String?,
    val imageUrl: Uri?
)