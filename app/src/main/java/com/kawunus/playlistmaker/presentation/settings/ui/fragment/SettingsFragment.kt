package com.kawunus.playlistmaker.presentation.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kawunus.playlistmaker.App
import com.kawunus.playlistmaker.databinding.FragmentSettingsBinding
import com.kawunus.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTheme().observe(viewLifecycleOwner) { isNight ->
            binding.themeSwitcher.isChecked = isNight
            (requireActivity().applicationContext as App).switchTheme(isNight)
        }
        binding.themeSwitcher.isChecked

        binding.shareView.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportView.setOnClickListener {
            viewModel.openSupport()
        }


        binding.agreementView.setOnClickListener {
            viewModel.openTerms()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }
}