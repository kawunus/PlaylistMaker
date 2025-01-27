package com.kawunus.playlistmaker.utils.converter

import com.kawunus.playlistmaker.data.db.entity.TrackEntity
import com.kawunus.playlistmaker.domain.model.track.Track

class TrackConverter {

    fun map(track: Track, addedAt: Long): TrackEntity = TrackEntity(
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