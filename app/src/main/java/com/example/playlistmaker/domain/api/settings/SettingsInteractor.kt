package com.example.playlistmaker.domain.api.settings

import com.example.playlistmaker.domain.model.settings.Theme

interface SettingsInteractor {

    fun getTheme(): Theme

    fun setTheme(theme: Theme)

    fun isFirstLaunch(): Boolean

    fun setFirstLaunchCompleted()
}