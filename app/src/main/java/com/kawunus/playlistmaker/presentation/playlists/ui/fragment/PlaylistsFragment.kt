package com.kawunus.playlistmaker.presentation.playlists.ui.fragment

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentPlaylistsBinding
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.presentation.library.ui.fragment.LibraryFragmentDirections
import com.kawunus.playlistmaker.presentation.playlists.ui.adapter.PlaylistAdapter
import com.kawunus.playlistmaker.presentation.playlists.ui.model.PlaylistState
import com.kawunus.playlistmaker.presentation.playlists.view_model.PlaylistViewModel
import com.kawunus.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment :
    BaseFragment<FragmentPlaylistsBinding, PlaylistViewModel>(FragmentPlaylistsBinding::inflate) {

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private var playlistAdapter: PlaylistAdapter? = null

    override val viewModel: PlaylistViewModel by viewModel()

    override fun initViews() = with(binding) {
        newPlaylistButton.setOnClickListener {
            val action = LibraryFragmentDirections.actionLibraryFragmentToNewPlaylistFragment(null)
            findNavController().navigate(action)
        }

        viewModel.getData()

        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false

        ) { playlist ->
            val action =
                LibraryFragmentDirections.actionLibraryFragmentToPlaylistInfoFragment(playlist)
            findNavController().navigate(action)
        }

        playlistAdapter = PlaylistAdapter { playlist ->
            onPlaylistClickDebounce(playlist)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter
    }

    override fun subscribe() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> renderContent(state.playlistList)
            PlaylistState.Empty -> renderEmptyState()
            PlaylistState.Loading -> renderLoadingState()
        }
    }

    private fun renderEmptyState() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        notFoundImageView.isVisible = true
        textView.isVisible = true
    }

    private fun renderLoadingState() = with(binding) {
        recyclerView.isVisible = false
        notFoundImageView.isVisible = false
        textView.isVisible = false
        progressBar.isVisible = true
    }

    private fun renderContent(playlistList: List<Playlist>) = with(binding) {
        notFoundImageView.isVisible = false
        textView.isVisible = false
        progressBar.isVisible = false
        recyclerView.isVisible = true
        playlistAdapter?.saveData(playlistList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}