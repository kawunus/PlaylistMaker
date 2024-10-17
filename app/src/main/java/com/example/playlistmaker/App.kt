package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.utilModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.model.settings.Theme
import com.example.playlistmaker.domain.prefs.PrefKeys
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    private val settingsInteractor: SettingsInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(repositoryModule, interactorModule, utilModule, viewModelModule, dataModule)
        }
        sharedPreferences = getSharedPreferences(
            PrefKeys.PREFS, MODE_PRIVATE
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