package com.kawunus.playlistmaker.presentation.settings.ui.fragment

import com.kawunus.playlistmaker.App
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentSettingsBinding
import com.kawunus.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment :
    BaseFragment<FragmentSettingsBinding, SettingsViewModel>(FragmentSettingsBinding::inflate) {

    override val viewModel: SettingsViewModel by viewModel()

    override fun initViews() = with(binding) {
        themeSwitcher.isChecked

        shareView.setOnClickListener {
            viewModel.shareApp()
        }

        supportView.setOnClickListener {
            viewModel.openSupport()
        }


        agreementView.setOnClickListener {
            viewModel.openTerms()
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

    override fun subscribe() = with(binding) {
        viewModel.observeTheme().observe(viewLifecycleOwner) { isNight ->
            themeSwitcher.isChecked = isNight
            (requireActivity().applicationContext as App).switchTheme(isNight)
        }
    }
}