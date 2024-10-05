package com.example.playlistmaker.domain.api.sharing

import com.example.playlistmaker.data.dto.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)

    fun openLink(link: String)

    fun openEmail(emailData: EmailData)

    fun getShareAppLink(): String

    fun getSupportEmailData(): EmailData

    fun getTermsLink(): String
}