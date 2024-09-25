package com.example.playlistmaker.domain.api.settings

import com.example.playlistmaker.domain.model.settings.Theme

interface SettingsRepository {
    fun getTheme(): Theme

    fun setTheme(theme: Theme)
}