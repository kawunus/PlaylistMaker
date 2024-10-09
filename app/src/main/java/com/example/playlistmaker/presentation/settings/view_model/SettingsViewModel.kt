package com.example.playlistmaker.presentation.settings.view_model

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.utils.creator.Creator

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    sharingInteractor = Creator.provideSharingInteractor(context),
                    settingsInteractor = Creator.provideSettingsInteractor(
                        context.getSharedPreferences(
                            PrefKeys.PREFS, MODE_PRIVATE
                        )
                    )
                )
            }
        }
    }

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