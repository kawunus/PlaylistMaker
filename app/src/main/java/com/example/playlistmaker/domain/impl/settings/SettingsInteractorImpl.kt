package com.example.playlistmaker.domain.impl.settings

import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.model.settings.Theme

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getTheme(): Theme {
        return settingsRepository.getTheme()
    }

}