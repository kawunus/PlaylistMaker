package com.example.playlistmaker.presentation.new_playlist.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.new_playlist.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private val binding by lazy {
        FragmentNewPlaylistBinding.inflate(layoutInflater)
    }

    private val viewModel: NewPlaylistViewModel by viewModel()

    override

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(content: Editable?) {
                if (!content.isNullOrEmpty()) {
                    createButton.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(), R.color.buttonCreateColorAble
                    )
                    createButton.isEnabled = true
                } else {
                    createButton.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(), R.color.buttonCreateColorDisable
                    )
                    createButton.isEnabled = false
                }
            }

        })

        binding.createButton.setOnClickListener {
            viewModel.createNewPlaylist(
                PlaylistEntity(
                    name = binding.nameEditText.text.toString(),
                    description = binding.descriptionEditText.text.toString(),
                    countOfTracks = 0,
                    trackList = emptyList(),
                    imageUrl = ""
                )
            )
        }
    }
}