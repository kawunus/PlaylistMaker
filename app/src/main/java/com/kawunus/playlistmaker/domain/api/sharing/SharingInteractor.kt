package com.kawunus.playlistmaker.domain.api.sharing

import com.kawunus.playlistmaker.domain.model.track.Track

interface SharingInteractor {

    fun shareApp()

    fun openTerms()

    fun openSupport()

    fun sharePlaylist(
        trackList: List<Track>,
        playlistName: String,
        playlistDescription: String?
    )
}