package com.kawunus.playlistmaker.presentation.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kawunus.playlistmaker.domain.api.settings.SettingsInteractor
import com.kawunus.playlistmaker.domain.api.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

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