package com.example.playlistmaker.presentation.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.search.SearchState
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.track.activity.TrackActivity
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.creator.Creator

class SearchActivity : AppCompatActivity() {

    private var editTextContext = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter { track ->
            if (viewModel.clickDebounce()) {
                viewModel.addToHistory(track)
                val intent = Intent(this, TrackActivity::class.java)
                intent.putExtra(IntentConsts.TRACK.name, track)
                startActivity(intent)
            }
        }
        historyAdapter = trackAdapter
        binding.historyRecyclerView.adapter = trackAdapter
        binding.recyclerView.adapter = trackAdapter

        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory(
                historyInteractor = Creator.provideHistoryInteractor(
                    getSharedPreferences(
                        PrefKeys.PREFS, MODE_PRIVATE
                    )
                )
            )
        )[SearchViewModel::class]

        viewModel.observeState().observe(this) { state ->
            render(state)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s?.isNotEmpty() == true && s.isNotBlank()) {
                    viewModel.onTextChanged(text = s.toString())
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
            viewModel.search(binding.editText.text.toString())
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) = with(binding) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextContext = savedInstanceState.getString("EDIT_TEXT_CONTEXT", "")
        editText.setText(editTextContext)

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
        trackAdapter.saveData(emptyList())
        hideHistory()
    }

    private fun showHistory(historyList: List<Track>) = with(binding) {
        progressBar.isVisible = false
        hideErrorViews()
        if (historyList.isNotEmpty()) {
            historyAdapter.saveData(historyList)
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
        trackAdapter.saveData(trackList)
        hideHistory()
        progressBar.isVisible = false
        recyclerView.isVisible = true
        hideErrorViews()
    }

    private fun hideKeyboard() = with(binding) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}
