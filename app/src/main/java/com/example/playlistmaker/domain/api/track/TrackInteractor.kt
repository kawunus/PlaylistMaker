package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.model.track.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(foundTracks: List<Track>, resultCode: Int)
    }
}