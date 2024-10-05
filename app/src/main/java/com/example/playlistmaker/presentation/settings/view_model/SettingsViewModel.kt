package com.example.playlistmaker.presentation.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
}