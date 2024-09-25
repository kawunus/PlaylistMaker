package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.utils.consts.MediaPlayerConsts

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun preparePlayer(track: Track) {
        mediaPlayerRepository.preparePlayer(track)
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun closePlayer() {
        mediaPlayerRepository.closePlayer()
    }

    override fun getPlayerState(): MediaPlayerConsts {
        return mediaPlayerRepository.getPlayerState()
    }

    override fun setResources(
        onPlayButton: () -> Unit,
        onPauseButton: () -> Unit,
        onSetTimer: (time: String) -> Unit
    ) {
        mediaPlayerRepository.setResources(onPlayButton, onPauseButton, onSetTimer)
    }
}