package com.example.playlistmaker.presentation.playlist_info.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.playlist_info.ui.model.TracksInPlaylistState
import com.example.playlistmaker.presentation.playlist_info.view_model.PlaylistInfoViewModel
import com.example.playlistmaker.presentation.search.ui.adapter.TrackAdapter
import com.example.playlistmaker.utils.converter.WordConverter
import com.example.playlistmaker.utils.debounce.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var binding: FragmentPlaylistInfoBinding
    private val viewModel: PlaylistInfoViewModel by viewModel {
        val args: PlaylistInfoFragmentArgs by navArgs()
        parametersOf(args.playlist)
    }

    private lateinit var playlistBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var trackList: List<Track> = emptyList()

    private lateinit var adapter: TrackAdapter

    var model: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlaylistInfoFragmentArgs by navArgs()
        model = args.playlist
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            val action =
                PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlayerFragment(track)
            findNavController().navigate(action)
        }
        adapter = TrackAdapter(onItemClick = { track ->
            onTrackClickDebounce(track)
        },
            onLongItemClick = { track ->
                showDialog(track)
            })
        binding.recyclerView.adapter = adapter

        viewModel.observeTracksInPlaylist().observe(viewLifecycleOwner) { state ->
            when (state) {
                is TracksInPlaylistState.Content -> {
                    model = state.playlist
                    renderModelViews(state.trackList)
                }

                is TracksInPlaylistState.Empty -> {
                    model = state.playlist
                    renderModelViews(trackList)
                }

                TracksInPlaylistState.Loading -> {
                    trackList = emptyList()
                }
            }
        }

        viewModel.getData()

        binding.navigationIconImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.shareImageView.setOnClickListener {
            sharePlaylist()
        }
        renderBottomSheets()
    }

    private fun sharePlaylist() {
        if (trackList.isEmpty()) {
            showToast(requireContext().getString(R.string.empty_playlist_message))
        } else {
            viewModel.sharePlaylist(trackList)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun renderBottomSheets() = with(binding) {
        val trackBottomSheetContainer = trackBottomSheet

        val trackBottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(trackBottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        trackBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        val bottomSheetContainer = playlistBottomSheet
        playlistBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        playlistBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        playlistBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowOverlay.visibility = View.GONE
                    }

                    else -> {
                        shadowOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.moreImageView.setOnClickListener {
            playlistBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun showDialog(track: Track) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_dialog_title)
            .setNegativeButton(R.string.dialog_no) { _, _ ->
            }
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)

            }.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
    }

    private fun renderModelViews(trackList: List<Track>) = with(binding) {
        Glide.with(requireContext()).load(model?.imageUrl).centerCrop()
            .placeholder(R.drawable.ic_single_playlist_placeholder).into(coverImageView)
        titleTextView.text = model?.name
        if (model?.description.isNullOrEmpty()) {
            descriptionTextView.isVisible = false
        } else {
            descriptionTextView.text = model?.description
        }
        val durationInMillis = trackList.sumOf {
            it.trackTimeMillis
        }
        val duration: String = WordConverter.getMinuteWordFromMillis(durationInMillis)
        durationOfTracksTextView.text = duration
        adapter.saveData(trackList)
        val countOfTracks =
            "${model?.countOfTracks} ${WordConverter.getTrackWordForm(model?.countOfTracks ?: 0)}"
        countOfTracksTextView.text = countOfTracks

        Glide.with(requireContext()).load(model?.imageUrl).centerCrop()
            .placeholder(R.drawable.playlist_placeholder).into(binding.miniCoverImageView)
        binding.nameTextView.text = model?.name
        binding.countTextView.text = countOfTracks
    }
}