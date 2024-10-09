package com.example.playlistmaker.domain.impl.sharing

import com.example.playlistmaker.data.dto.EmailData
import com.example.playlistmaker.domain.api.sharing.ExternalNavigator
import com.example.playlistmaker.domain.api.sharing.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getShareAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return externalNavigator.getSupportEmailData()
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermsLink()
    }
}