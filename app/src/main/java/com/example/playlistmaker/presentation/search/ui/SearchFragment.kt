package com.example.playlistmaker.presentation.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.search.SearchState
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var editTextContext = ""
    private lateinit var binding: FragmentSearchBinding

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var trackAdapter: TrackAdapter? = null
    private var historyAdapter: TrackAdapter? = null

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track: Track ->
            viewModel.addToHistory(track)
            findNavController().navigate(R.id.action_searchFragment_to_trackActivity,
                Bundle().apply {
                    putParcelable(IntentConsts.TRACK.name, track)
                })
        }

        trackAdapter = TrackAdapter { track ->
            onTrackClickDebounce(track)
        }
        historyAdapter = trackAdapter
        binding.historyRecyclerView.adapter = trackAdapter
        binding.recyclerView.adapter = trackAdapter


        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s?.isNotEmpty() == true && s.isNotBlank()) {
                    viewModel.searchDebounce(changedText = s.toString())
                } else {
                    viewModel.showHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.editText.addTextChangedListener(editTextWatcher)

        binding.clearIcon.setOnClickListener {
            viewModel.clearRequestText()
            binding.editText.setText("")
        }

        binding.updateButton.setOnClickListener {
            viewModel.searchDebounce(binding.editText.text.toString())
        }

        binding.historyButton.setOnClickListener {
            viewModel.clearHistory()
            hideHistory()
        }

        viewModel.showHistory()
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EDIT_TEXT_CONTEXT", editTextContext)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            val editTextContext = it.getString("EDIT_TEXT_CONTEXT", "")
            binding.editText.setText(editTextContext)
        }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        binding.progressBar.isVisible = false
    }


    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.trackList)
            SearchState.Empty -> showEmpty()
            SearchState.Error -> showError()
            is SearchState.History -> showHistory(state.historyList)
            SearchState.Loading -> showLoading()
            SearchState.PreLoading -> showPreLoading()
        }
    }

    private fun showPreLoading() = with(binding) {
        binding.progressBar.isVisible = false
        hideErrorViews()
        clearIcon.isVisible = true
    }

    private fun showLoading() = with(binding) {
        hideErrorViews()
        progressBar.isVisible = true
        recyclerView.isVisible = false
        trackAdapter?.saveData(emptyList())
        hideHistory()
    }

    private fun showHistory(historyList: List<Track>) = with(binding) {
        progressBar.isVisible = false
        hideErrorViews()
        recyclerView.isVisible = false
        historyAdapter?.saveData(emptyList())
        if (historyList.isNotEmpty()) {
            historyAdapter?.saveData(historyList)
            historyRecyclerView.isVisible = true
            historyButton.isVisible = true
            historyTextView.isVisible = true
            clearIcon.isVisible = false
        } else {
            hideHistory()
        }
    }

    private fun hideHistory() = with(binding) {
        historyRecyclerView.isVisible = false
        historyButton.isVisible = false
        historyTextView.isVisible = false
    }

    private fun showError() = with(binding) {
        hideHistory()
        updateButton.isVisible = true
        errorText.isVisible = true
        errorImage.isVisible = true
        errorText.setText(R.string.no_internet)
        errorImage.setImageResource(R.drawable.ic_no_internet)
        errorLinear.isVisible = true
        recyclerView.isVisible = false
        historyRecyclerView.isVisible = false
        progressBar.isVisible = false
    }

    private fun showEmpty() = with(binding) {
        hideHistory()
        errorLinear.isVisible = true
        errorText.isVisible = true
        errorImage.isVisible = true
        errorText.setText(R.string.not_found)
        errorImage.setImageResource(R.drawable.ic_not_found)
        recyclerView.isVisible = false
        historyRecyclerView.isVisible = false
        progressBar.isVisible = false
    }

    private fun hideErrorViews() = with(binding) {
        errorLinear.isVisible = false
        errorText.isVisible = false
        errorImage.isVisible = false
        updateButton.isVisible = false
    }


    private fun showContent(trackList: List<Track>) = with(binding) {
        hideHistory()
        trackAdapter?.saveData(trackList)
        progressBar.isVisible = false
        recyclerView.isVisible = true
        hideErrorViews()
    }

    private fun hideKeyboard() = with(binding) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackAdapter = null
        historyAdapter = null
        binding.recyclerView.adapter = null
        binding.historyRecyclerView.adapter = null
    }
}