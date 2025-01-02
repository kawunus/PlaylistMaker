package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.track.Track

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository,
) :
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

    override fun setLambdas(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayerRepository.setLambdas(
            onPrepared = onPrepared,
            onCompletion = onCompletion
        )
    }

    override fun getCurrentPlayerPosition(): String {
        return mediaPlayerRepository.getCurrentPlayerPosition()
    }
}