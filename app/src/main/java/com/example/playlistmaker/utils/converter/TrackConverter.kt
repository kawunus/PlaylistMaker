package com.example.playlistmaker.utils.converter

import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.domain.model.track.Track

class TrackConverter {

    fun map(track: Track): FavoriteTrackEntity = FavoriteTrackEntity(
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
        addedAt = System.currentTimeMillis()
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