package com.example.playlistmaker.presentation.new_playlist.ui.fragment

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.new_playlist.ui.model.NewPlaylistState
import com.example.playlistmaker.presentation.new_playlist.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private val binding by lazy {
        FragmentNewPlaylistBinding.inflate(layoutInflater)
    }

    private val viewModel: NewPlaylistViewModel by viewModel()

    private var coverUrl: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
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

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                NewPlaylistState.Created -> {
                    Toast.makeText(
                        requireContext(),
                        "Плейлист ${binding.nameEditText.text.toString()} создан",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }

                NewPlaylistState.NotCreated -> {

                }
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addImageImageView.setImageURI(uri)
                    coverUrl = uri
                } else {
                    Log.e("PHOTO_PICKER", "No media selected")
                }
            }

        binding.addImageImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            viewModel.createNewPlaylist(
                name = binding.nameEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                imageUrl = coverUrl
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            if (binding.descriptionEditText.text.isNullOrEmpty() && binding.nameEditText.text.isNullOrEmpty() && coverUrl == null) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.descriptionEditText.text.isNullOrEmpty() && binding.nameEditText.text.isNullOrEmpty() && coverUrl == null) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ ->
            }
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().popBackStack()
            }.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultTextColor))
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultTextColor))

    }
}