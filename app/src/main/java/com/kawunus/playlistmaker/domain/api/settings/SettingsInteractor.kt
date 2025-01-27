package com.kawunus.playlistmaker.domain.api.settings

import com.kawunus.playlistmaker.domain.model.settings.Theme

interface SettingsInteractor {

    fun getTheme(): Theme

    fun setTheme(theme: Theme)

    fun isFirstLaunch(): Boolean

    fun setFirstLaunchCompleted()
}