package com.example.playlistmaker.presentation.playlists.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.presentation.playlists.ui.adapter.PlaylistAdapter
import com.example.playlistmaker.presentation.playlists.ui.model.PlaylistState
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding get() = _binding!!

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private var playlistAdapter: PlaylistAdapter? = null

    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.getData()

        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false

        ) { playlist ->
            // TODO: Переход на экран плейлиста
        }

        playlistAdapter = PlaylistAdapter { playlist ->
            onPlaylistClickDebounce(playlist)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = playlistAdapter
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