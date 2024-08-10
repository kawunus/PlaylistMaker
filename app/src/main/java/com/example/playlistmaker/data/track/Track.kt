package com.example.playlistmaker.data.track

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    val artworkUrl100: String,
    val trackId: Long,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val collectionName: String
) : Parcelable