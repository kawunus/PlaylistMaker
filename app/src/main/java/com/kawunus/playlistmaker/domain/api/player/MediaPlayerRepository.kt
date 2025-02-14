package com.kawunus.playlistmaker.domain.api.player

import com.kawunus.playlistmaker.domain.model.track.Track

interface MediaPlayerRepository {

    fun preparePlayer(track: Track)

    fun pausePlayer()

    fun startPlayer()

    fun closePlayer()

    fun setLambdas(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun getCurrentPlayerPosition(): String

}