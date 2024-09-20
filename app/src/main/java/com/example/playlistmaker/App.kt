package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.utils.consts.SettingsPrefs
import com.example.playlistmaker.utils.creator.Creator

class App : Application() {
    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(
            PrefKeys.PREFS, MODE_PRIVATE
        )

        val creator = Creator()

        val settingsInteractor = creator.provideSettingsInteractor(
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

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreferences.edit().putBoolean(SettingsPrefs.THEME, darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}