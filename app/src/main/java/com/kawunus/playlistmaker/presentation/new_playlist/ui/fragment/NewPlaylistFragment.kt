package com.kawunus.playlistmaker.presentation.new_playlist.ui.fragment

import android.app.AlertDialog
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.kawunus.playlistmaker.domain.model.playlist.Playlist
import com.kawunus.playlistmaker.presentation.new_playlist.ui.model.NewPlaylistState
import com.kawunus.playlistmaker.presentation.new_playlist.view_model.NewPlaylistViewModel
import com.kawunus.playlistmaker.utils.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment :
    BaseFragment<FragmentNewPlaylistBinding, NewPlaylistViewModel>(FragmentNewPlaylistBinding::inflate) {

    private var model: Playlist? = null

    override val viewModel: NewPlaylistViewModel by viewModel()

    private var coverUrl: Uri? = null

    override fun initViews() = with(binding) {
        val args: NewPlaylistFragmentArgs by navArgs()
        model = args.playlist

        if (model != null) {
            nameEditText.setText(model?.name)
            descriptionEditText.setText(model?.description ?: "")

            addImageImageView.load(model?.imageUrl) {
                placeholder(R.drawable.ic_add_playlist)
                error(R.drawable.ic_add_playlist)
            }

            createButton.text = getString(R.string.playlist_save)
            createButton.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), R.color.buttonCreateColorAble
            )
            createButton.isEnabled = true
            binding.toolbar.title = getString(R.string.playlist_edit_title)
            coverUrl
        }


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

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addImageImageView.setImageURI(uri)
                    coverUrl = uri
                } else {
                    Log.e("PHOTO_PICKER", "No media selected")
                }
            }

        addImageImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        if (model == null) {
            createButton.setOnClickListener {
                viewModel.createNewPlaylist(
                    name = nameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    imageUrl = coverUrl
                )
            }
        } else {
            createButton.setOnClickListener {
                viewModel.updatePlaylist(
                    name = nameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    imageUrl = coverUrl,
                    playlist = model!!
                )
            }
        }
        toolbar.setNavigationOnClickListener {
            if ((descriptionEditText.text.isNullOrEmpty() && nameEditText.text.isNullOrEmpty() && coverUrl == null) || model != null) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }
        setOnBackPressed()
    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if ((binding.descriptionEditText.text.isNullOrEmpty() && binding.nameEditText.text.isNullOrEmpty() && coverUrl == null) || model != null) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }
    }

    override fun subscribe() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                NewPlaylistState.Created -> {
                    if (model == null) {
                        showSnackBar(
                            getString(
                                R.string.playlist_created,
                                binding.nameEditText.text.toString().trim()
                            )
                        )
                        findNavController().popBackStack()
                    } else {
                        showSnackBar(
                            getString(
                                R.string.playlist_edit_ok,
                                binding.nameEditText.text.toString().trim()
                            )
                        )
                        findNavController().popBackStack()
                    }
                }

                NewPlaylistState.NotCreated -> {
                }
            }
        }
    }

    private fun showDialog() {
        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.playlist_dialog_title)
                .setMessage(R.string.playlist_dialog_message)
                .setNeutralButton(R.string.playlist_dialog_neutral) { _, _ ->
                }.setPositiveButton(R.string.playlist_dialog_positive) { _, _ ->
                    findNavController().popBackStack()
                }.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultTextColor))
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultTextColor))

    }

    private fun showSnackBar(message: String) {
        Snackbar.showSnackbar(binding.root, message, requireContext())
    }
}