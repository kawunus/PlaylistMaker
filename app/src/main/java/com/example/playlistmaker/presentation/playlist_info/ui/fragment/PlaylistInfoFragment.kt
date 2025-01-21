package com.example.playlistmaker.presentation.playlist_info.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.playlist_info.ui.model.TracksInPlaylistState
import com.example.playlistmaker.presentation.playlist_info.view_model.PlaylistInfoViewModel
import com.example.playlistmaker.utils.converter.WordConverter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistInfoBinding
    private val viewModel: PlaylistInfoViewModel by viewModel {
        val args: PlaylistInfoFragmentArgs by navArgs()
        parametersOf(args.playlist)
    }

    private var trackList: List<Track> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlaylistInfoFragmentArgs by navArgs()
        val model: Playlist = args.playlist

        viewModel.observeTracksInPlaylist().observe(viewLifecycleOwner) { state ->
            when (state) {
                is TracksInPlaylistState.Content -> {
                    val durationInMillis = state.trackList.sumOf {
                        it.trackTimeMillis
                    }
                    val duration: String = WordConverter.getMinuteWordFromMillis(durationInMillis)
                    binding.durationOfTracksTextView.text = duration
                    trackList = state.trackList
                }

                TracksInPlaylistState.Empty -> {
                    val duration: String = WordConverter.getMinuteWordFromMillis(0)
                    binding.durationOfTracksTextView.text = duration
                    trackList = emptyList()
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

        Glide.with(requireContext()).load(model.imageUrl).centerCrop()
            .placeholder(R.drawable.ic_single_playlist_placeholder).into(binding.coverImageView)
        binding.titleTextView.text = model.name
        if (model.description.isNullOrEmpty()) {
            binding.descriptionTextView.isVisible = false
        } else {
            binding.descriptionTextView.text = model.description
        }
        val countOfTracks: String =
            "${model.countOfTracks} ${WordConverter.getTrackWordForm(model.countOfTracks)}"
        binding.countOfTracksTextView.text = countOfTracks

        binding.shareImageView.setOnClickListener {
            sharePlaylist()
        }
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
}