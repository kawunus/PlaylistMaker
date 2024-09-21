package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts

interface MediaPlayerRepository {

    fun preparePlayer(track: Track)

    fun pausePlayer()

    fun startPlayer()

    fun closePlayer()

    fun getPlayerState(): MediaPlayerConsts

    fun setResources(
        onPlayButton: () -> Unit,
        onPauseButton: () -> Unit,
        onSetTimer: (time: String) -> Unit
    )

}