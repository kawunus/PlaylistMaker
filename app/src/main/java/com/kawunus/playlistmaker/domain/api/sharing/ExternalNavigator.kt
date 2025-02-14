package com.kawunus.playlistmaker.domain.api.sharing

import com.kawunus.playlistmaker.data.dto.EmailData
import com.kawunus.playlistmaker.domain.model.track.Track

interface ExternalNavigator {
    fun shareLink(link: String)

    fun openLink(link: String)

    fun openEmail(emailData: EmailData)

    fun getShareAppLink(): String

    fun getSupportEmailData(): EmailData

    fun getTermsLink(): String

    fun sharePlaylist(
        trackList: List<Track>,
        playlistName: String,
        playlistDescription: String?
    )
}