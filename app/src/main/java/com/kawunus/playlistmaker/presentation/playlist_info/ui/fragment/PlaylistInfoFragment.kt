package com.kawunus.playlistmaker.presentation.playlist_info.ui.fragment

import android.app.AlertDialog
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.playlist_info.ui.model.TracksInPlaylistState
import com.kawunus.playlistmaker.presentation.playlist_info.view_model.PlaylistInfoViewModel
import com.kawunus.playlistmaker.presentation.search.ui.adapter.TrackAdapter
import com.kawunus.playlistmaker.utils.converter.WordConverter
import com.kawunus.playlistmaker.utils.debounce.debounce
import com.kawunus.playlistmaker.utils.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment :
    BaseFragment<FragmentPlaylistInfoBinding, PlaylistInfoViewModel>(FragmentPlaylistInfoBinding::inflate) {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override val viewModel: PlaylistInfoViewModel by viewModel {
        val args: PlaylistInfoFragmentArgs by navArgs()
        parametersOf(args.playlist)
    }

    private lateinit var playlistBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var trackList: List<Track> = emptyList()

    private lateinit var adapter: TrackAdapter

    var model: Playlist? = null

    override fun initViews() = with(binding) {
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
        }, onLongItemClick = { track ->
            showTrackDeleteDialog(track)
        })
        recyclerView.adapter = adapter

        viewModel.getData()

        navigationIconImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        shareImageView.setOnClickListener {
            sharePlaylist()
        }
        renderBottomSheets()

        deletePlaylistTextView.setOnClickListener {
            showPlaylistDeleteDialog()
        }

        sharePlaylistTextView.setOnClickListener {
            sharePlaylist()
            playlistBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        editPlaylistTextView.setOnClickListener {
            val action =
                PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToNewPlaylistFragment(model)
            findNavController().navigate(action)
        }
    }

    override fun subscribe() {
        viewModel.observeTracksInPlaylist().observe(viewLifecycleOwner) { state ->
            when (state) {
                is TracksInPlaylistState.Content -> {
                    model = state.playlist
                    trackList = state.trackList
                    renderModelViews(state.trackList)
                }

                is TracksInPlaylistState.Empty -> {
                    model = state.playlist
                    trackList = emptyList()
                    renderModelViews(emptyList())
                    showSnackBar(getString(R.string.playlist_empty_snackbar, model?.name))
                }

                TracksInPlaylistState.Loading -> {
                    trackList = emptyList()
                }
            }
        }

        viewModel.observeDeleteState().observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                findNavController().popBackStack()
            }
        }
    }

    private fun sharePlaylist() {
        if (trackList.isEmpty()) {
            showSnackBar(requireContext().getString(R.string.empty_playlist_message))
        } else {
            viewModel.sharePlaylist(trackList)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.showSnackbar(binding.root, message, requireContext())
    }

    private fun renderBottomSheets() = with(binding) {
        val trackBottomSheetContainer = trackBottomSheet

        val trackBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout> =
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

    private fun showTrackDeleteDialog(track: Track) {
        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.delete_dialog_title)
                .setNegativeButton(R.string.dialog_no) { _, _ ->
                }.setPositiveButton(R.string.dialog_yes) { _, _ ->
                    viewModel.deleteTrackFromPlaylist(track)

                }.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
    }

    private fun showPlaylistDeleteDialog() {
        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.playlist_delete))
                .setMessage(
                    requireContext().getString(
                        R.string.playlist_delete_dialog_title, model?.name
                    )
                ).setNegativeButton(R.string.dialog_no) { _, _ ->
                }.setPositiveButton(R.string.dialog_yes) { _, _ ->
                    viewModel.deletePlaylist()

                }.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor))
    }

    private fun renderModelViews(trackList: List<Track>) = with(binding) {
        coverImageView.load(model?.imageUrl) {
            placeholder(R.drawable.ic_single_playlist_placeholder)
            error(R.drawable.ic_single_playlist_placeholder)
        }
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
        miniCoverImageView.load(model?.imageUrl) {
            placeholder(R.drawable.playlist_placeholder)
            error(R.drawable.playlist_placeholder)
        }
        binding.nameTextView.text = model?.name
        binding.countTextView.text = countOfTracks
    }
}