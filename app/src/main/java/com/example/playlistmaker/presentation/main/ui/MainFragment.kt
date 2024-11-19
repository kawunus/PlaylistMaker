package com.example.playlistmaker.presentation.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.presentation.search.activity.SearchActivity
import com.example.playlistmaker.presentation.settings.activity.SettingsActivity

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFind.setOnClickListener {
            val findIntent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(findIntent)
        }

        binding.buttonLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
        }

        binding.buttonSettings.setOnClickListener {
            val settingsIntent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}