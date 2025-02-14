package com.kawunus.playlistmaker.domain.impl.settings

import com.kawunus.playlistmaker.domain.api.settings.SettingsInteractor
import com.kawunus.playlistmaker.domain.api.settings.SettingsRepository
import com.kawunus.playlistmaker.domain.model.settings.Theme

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getTheme(): Theme {
        return settingsRepository.getTheme()
    }

    override fun setTheme(theme: Theme) {
        settingsRepository.setTheme(theme)
    }

    override fun isFirstLaunch(): Boolean {
        return settingsRepository.isFirstLaunch()
    }

    override fun setFirstLaunchCompleted() {
        settingsRepository.setFirstLaunchCompleted()
    }

}