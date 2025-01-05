package com.example.playlistmaker.presentation.favorites.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.favorites.ui.model.FavoritesState
import com.example.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.presentation.search.ui.TrackAdapter
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var trackAdapter: TrackAdapter? = null

    private val viewModel: FavoritesViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track: Track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_trackActivity,
                Bundle().apply {
                    putParcelable(IntentConsts.TRACK.name, track)
                })
        }

        trackAdapter = TrackAdapter { track ->
            onTrackClickDebounce(track)
        }

        binding.recyclerView.adapter = trackAdapter
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> renderContent(state.trackList)
            FavoritesState.Empty -> {
                renderEmptyState()
            }

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
}