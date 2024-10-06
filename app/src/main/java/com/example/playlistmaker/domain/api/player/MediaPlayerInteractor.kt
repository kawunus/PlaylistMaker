package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.model.track.Track

interface MediaPlayerInteractor {

    fun preparePlayer(track: Track)

    fun pausePlayer()

    fun startPlayer()

    fun closePlayer()
}