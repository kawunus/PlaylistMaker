package com.kawunus.playlistmaker.presentation.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kawunus.playlistmaker.R
import com.kawunus.playlistmaker.core.ui.BaseFragment
import com.kawunus.playlistmaker.databinding.FragmentSearchBinding
import com.kawunus.playlistmaker.domain.model.track.Track
import com.kawunus.playlistmaker.presentation.search.ui.adapter.TrackAdapter
import com.kawunus.playlistmaker.presentation.search.ui.model.SearchState
import com.kawunus.playlistmaker.presentation.search.view_model.SearchViewModel
import com.kawunus.playlistmaker.utils.debounce.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {

    private var editTextContext = ""

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var trackAdapter: TrackAdapter? = null
    private var historyAdapter: TrackAdapter? = null

    override val viewModel: SearchViewModel by viewModel()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    override fun initViews() = with(binding) {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track: Track ->
            viewModel.addToHistory(track)
            val action = SearchFragmentDirections.actionSearchFragmentToTrackActivity(track)
            findNavController().navigate(action)
        }

        trackAdapter = TrackAdapter(onLongItemClick = null, onItemClick = { track ->
            onTrackClickDebounce(track)
        })
        historyAdapter = trackAdapter
        historyRecyclerView.adapter = trackAdapter
        recyclerView.adapter = trackAdapter


        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearIcon.visibility = clearButtonVisibility(s)
                if (s?.isNotEmpty() == true && s.isNotBlank()) {
                    viewModel.searchDebounce(changedText = s.toString())
                } else {
                    viewModel.showHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editText.addTextChangedListener(editTextWatcher)

        clearIcon.setOnClickListener {
            viewModel.clearRequestText()
            editText.setText("")
        }

        updateButton.setOnClickListener {
            viewModel.searchDebounce(editText.text.toString())
        }

        historyButton.setOnClickListener {
            viewModel.clearHistory()
            hideHistory()
        }

        viewModel.showHistory()
    }

    override fun subscribe() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
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
            editText.setText(editTextContext)
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
        binding?.recyclerView?.adapter = null
        binding?.historyRecyclerView?.adapter = null

        trackAdapter = null
        historyAdapter = null

        super.onDestroyView()
    }
}