package com.example.playlistmaker.domain.model.playlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val imageName: String?,
    val countOfTracks: Int,
) : Parcelable