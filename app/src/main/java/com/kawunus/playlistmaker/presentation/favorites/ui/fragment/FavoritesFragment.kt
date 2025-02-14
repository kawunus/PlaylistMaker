package com.kawunus.playlistmaker.presentation.favorites.ui.fragment

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentFavoritesBinding
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.favorites.ui.model.FavoritesState
import com.kawunus.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.kawunus.playlistmaker.presentation.library.ui.fragment.LibraryFragmentDirections
import com.kawunus.playlistmaker.presentation.search.ui.adapter.TrackAdapter
import com.kawunus.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(FragmentFavoritesBinding::inflate) {

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var trackAdapter: TrackAdapter? = null

    override val viewModel: FavoritesViewModel by viewModel()

    override fun initViews() {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track: Track ->
            val action = LibraryFragmentDirections.actionLibraryFragmentToTrackActivity(track)
            findNavController().navigate(action)
        }

        trackAdapter = TrackAdapter(onLongItemClick = null, onItemClick = { track ->
            onTrackClickDebounce(track)
        })

        binding.recyclerView.adapter = trackAdapter
    }

    override fun subscribe() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.getData()
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> renderContent(state.trackList)
            FavoritesState.Empty -> renderEmptyState()
            FavoritesState.Loading -> renderLoadingState()
        }
    }

    private fun renderEmptyState() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        titleTextView.isVisible = true
        notFoundImageView.isVisible = true
    }

    private fun renderLoadingState() = with(binding) {
        recyclerView.isVisible = false
        titleTextView.isVisible = false
        notFoundImageView.isVisible = false
        progressBar.isVisible = true
    }

    private fun renderContent(trackList: List<Track>) = with(binding) {
        progressBar.isVisible = false
        titleTextView.isVisible = false
        notFoundImageView.isVisible = false
        recyclerView.isVisible = true
        trackAdapter?.saveData(trackList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}