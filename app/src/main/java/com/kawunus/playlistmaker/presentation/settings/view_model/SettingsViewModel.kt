package com.kawunus.playlistmaker.presentation.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kawunus.playlistmaker.core.ui.BaseViewModel
import com.kawunus.playlistmaker.domain.api.settings.SettingsInteractor
import com.kawunus.playlistmaker.domain.api.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    settingsInteractor: SettingsInteractor
) : BaseViewModel() {

    private val themeLiveData = MutableLiveData<Boolean>()

    fun observeTheme(): LiveData<Boolean> = themeLiveData

    init {
        themeLiveData.value = settingsInteractor.getTheme().isNight
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun switchTheme(isNight: Boolean) {
        themeLiveData.value = isNight
    }
}