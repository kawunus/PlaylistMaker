package com.kawunus.playlistmaker.presentation.library.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kawunus.playlistmaker.presentation.favorites.ui.fragment.FavoritesFragment
import com.kawunus.playlistmaker.presentation.playlists.ui.fragment.PlaylistsFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}