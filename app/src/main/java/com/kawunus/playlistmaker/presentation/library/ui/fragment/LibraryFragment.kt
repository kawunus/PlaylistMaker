package com.kawunus.playlistmaker.presentation.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.databinding.CustomTabBinding
import com.kawunus.playlistmaker.databinding.FragmentLibraryBinding
import com.kawunus.playlistmaker.presentation.library.ui.adapter.LibraryViewPagerAdapter

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter =
            LibraryViewPagerAdapter(fragmentManager = childFragmentManager, lifecycle = lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabBinding = CustomTabBinding.inflate(LayoutInflater.from(tab.view.context))
            tabBinding.tabTextView.text = when (position) {
                0 -> getString(R.string.favorites)
                1 -> getString(R.string.playlists)
                else -> ""
            }
            tab.customView = tabBinding.root
        }

        tabMediator.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}