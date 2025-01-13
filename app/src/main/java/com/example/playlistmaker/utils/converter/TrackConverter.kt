package com.example.playlistmaker.utils.converter

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.model.track.Track

class TrackConverter {

    fun map(track: Track, addedAt: Long, isFavorite: Boolean): TrackEntity = TrackEntity(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTimeMillis = track.trackTimeMillis,
        artworkUrl100 = track.artworkUrl100,
        country = track.country,
        primaryGenreName = track.primaryGenreName,
        collectionName = track.collectionName,
        previewUrl = track.previewUrl,
        releaseDate = track.releaseDate,
        addedAt = addedAt,
        isFavorite = isFavorite
    )

    fun map(track: TrackEntity): Track = Track(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTimeMillis = track.trackTimeMillis,
        artworkUrl100 = track.artworkUrl100,
        country = track.country,
        primaryGenreName = track.primaryGenreName,
        collectionName = track.collectionName,
        previewUrl = track.previewUrl,
        releaseDate = track.releaseDate
    )
}