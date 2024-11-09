package com.example.playlistmaker.presentation.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.databinding.CustomTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter =
            LibraryViewPagerAdapter(fragmentManager = supportFragmentManager, lifecycle = lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabBinding = CustomTabBinding.inflate(LayoutInflater.from(tab.view.context))
            tabBinding.tabTextView.text = when (position) {
                0 -> getString(R.string.favorites)
                1 -> getString(R.string.playlists)
                else -> ""
            }
            tab.customView = tabBinding.root
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}