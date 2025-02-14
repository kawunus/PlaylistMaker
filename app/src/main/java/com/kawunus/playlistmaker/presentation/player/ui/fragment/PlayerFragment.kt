package com.kawunus.playlistmaker.presentation.player.ui.fragment

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentPlayerBinding
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.player.ui.adapter.PlaylistInPlayerAdapter
import com.kawunus.playlistmaker.presentation.player.ui.model.AdditionState
import com.kawunus.playlistmaker.presentation.player.ui.model.PlayerPlaylistState
import com.kawunus.playlistmaker.presentation.player.ui.model.PlayerState
import com.kawunus.playlistmaker.presentation.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment :
    BaseFragment<FragmentPlayerBinding, PlayerViewModel>(FragmentPlayerBinding::inflate) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var adapter: PlaylistInPlayerAdapter? = null

    override val viewModel: PlayerViewModel by viewModel {
        val args: PlayerFragmentArgs by navArgs()
        parametersOf(args.track)
    }

    override fun initViews() = with(binding) {
        val args: PlayerFragmentArgs by navArgs()
        val model = args.track

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        renderModelInformation(model)

        likeButton.setOnClickListener {
            viewModel.likeButtonControl()
        }

        playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.preparePlayer()

        adapter = PlaylistInPlayerAdapter { playlist ->
            viewModel.addTrackToPlaylist(
                track = model, playlist = playlist
            )
        }

        recyclerView.adapter = adapter

        viewModel.getPlaylists()

        renderBottomSheet()
    }

    override fun subscribe() = with(binding) {
        viewModel.observeIsFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                likeButton.setImageResource(R.drawable.ic_is_liked)
            } else {
                likeButton.setImageResource(R.drawable.ic_like)
            }
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) { playerState ->
            when (playerState!!) {
                is PlayerState.Default -> {
                    playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Paused -> {
                    playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Playing -> {
                    playButton.setImageResource(R.drawable.ic_pause)
                }

                is PlayerState.Prepared -> {
                    playButton.setImageResource(R.drawable.ic_play)
                }
            }
            currentTimeTextView.text = playerState.progress
            playButton.isEnabled = playerState.isPlayButtonEnabled
        }

        viewModel.observePlayerPlaylistState().observe(viewLifecycleOwner) { playlistState ->
            when (playlistState) {
                is PlayerPlaylistState.Content -> renderContent(playlistState.playlistList)
                PlayerPlaylistState.Empty -> renderEmpty()
            }
        }

        viewModel.observeAdditionStatus().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdditionState.Error -> {
                    showToast(getString(R.string.bottom_sheet_failed_addition, state.playlistName))
                }

                is AdditionState.Successful -> {
                    showToast(
                        getString(
                            R.string.bottom_sheet_successful_addition, state.playlistName
                        )
                    )

                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun renderEmpty() {
        adapter?.saveData(emptyList())
    }

    private fun renderContent(playlistList: List<Playlist>) {
        adapter?.saveData(playlistList)
    }

    private fun renderModelInformation(model: Track) = with(binding) {

        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        trackNameTextView.text = model.trackName
        artistNameTextView.text = model.artistName
        trackTimeTextView.text = dateFormatter.format(model.trackTimeMillis)
        imageView.load(model.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")) {
            placeholder(R.drawable.track_placeholder)
            error(R.drawable.track_placeholder)
            transformations(RoundedCornersTransformation(8f))
        }
        trackCountryTextView.text = model.country
        trackGenreTextView.text = model.primaryGenreName
        trackYearTextView.text = model.releaseDate.substring(0, 4)
        currentTimeTextView.text = getString(R.string.track_current_time)

        if (model.collectionName.isEmpty()) {
            yearTextView.isVisible = false
            trackYearTextView.isVisible = false
        } else {
            trackAlbumTextView.text = model.collectionName
        }
    }

    private fun renderBottomSheet() = with(binding) {
        val bottomSheetContainer = bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
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

        playlistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        newPlaylistButton.setOnClickListener {
            viewModel.destroyPlayer()
            val action = PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment(null)
            findNavController().navigate(action)
        }
    }
}

