package com.kawunus.playlistmaker.utils.converter

import com.kawunus.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.kawunus.playlistmaker.domain.model.track.Track

class FavoriteTrackConverter {

    fun map(track: Track, addedAt: Long): FavoriteTrackEntity =
        FavoriteTrackEntity(
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

    fun map(track: FavoriteTrackEntity): Track = Track(
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