package com.example.playlistmaker.presentation.player.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.player.ui.adapter.PlaylistInPlayerAdapter
import com.example.playlistmaker.presentation.player.ui.model.AdditionState
import com.example.playlistmaker.presentation.player.ui.model.PlayerPlaylistState
import com.example.playlistmaker.presentation.player.ui.model.PlayerState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var adapter: PlaylistInPlayerAdapter? = null

    private val viewModel: PlayerViewModel by viewModel {
        val args: PlayerFragmentArgs by navArgs()
        parametersOf(args.track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlayerFragmentArgs by navArgs()
        val model = args.track

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        renderModelInformation(model)

        binding.likeButton.setOnClickListener {
            viewModel.likeButtonControl()
        }

        viewModel.observeIsFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.likeButton.setImageResource(R.drawable.ic_is_liked)
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like)
            }
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) { playerState ->
            when (playerState!!) {
                is PlayerState.Default -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }

                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.ic_pause)
                }

                is PlayerState.Prepared -> {
                    binding.playButton.setImageResource(R.drawable.ic_play)
                }
            }
            Log.e("PLAYER_STATE", "$playerState")
            binding.currentTimeTextView.text = playerState.progress
            binding.playButton.isEnabled = playerState.isPlayButtonEnabled
        }
        viewModel.preparePlayer()

        viewModel.observePlayerPlaylistState().observe(viewLifecycleOwner) { playlistState ->
            when (playlistState) {
                is PlayerPlaylistState.Content -> renderContent(playlistState.playlistList)
                PlayerPlaylistState.Empty -> renderEmpty()
            }
        }

        adapter = PlaylistInPlayerAdapter { playlist ->
            viewModel.addTrackToPlaylist(
                track = model,
                playlist = playlist
            )
        }

        binding.recyclerView.adapter = adapter

        viewModel.getPlaylists()

        renderBottomSheet()

        viewModel.observeAdditionStatus().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdditionState.Error -> {
                    showToast(getString(R.string.bottom_sheet_failed_addition, state.playlistName))
                }

                is AdditionState.Successful -> {
                    showToast(
                        getString(
                            R.string.bottom_sheet_successful_addition,
                            state.playlistName
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
        Glide.with(requireContext())
            .load(model.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(
                RoundedCorners(8)
            ).placeholder(R.drawable.track_placeholder).into(imageView)
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
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }
}

