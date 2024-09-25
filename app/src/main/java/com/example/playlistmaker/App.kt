package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.model.settings.Theme
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.utils.creator.Creator

class App : Application() {
    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(
            PrefKeys.PREFS, MODE_PRIVATE
        )

        settingsInteractor = Creator.provideSettingsInteractor(
            getSharedPreferences(
                PrefKeys.PREFS, MODE_PRIVATE
            )
        )

        darkTheme = settingsInteractor.getTheme().isNight
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(isNight: Boolean) {
        darkTheme = isNight
        settingsInteractor.setTheme(theme = Theme(isNight))
        AppCompatDelegate.setDefaultNightMode(
            if (isNight) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}