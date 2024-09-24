package com.example.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.presentation.track.TrackActivity
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.creator.Creator

class SearchActivity : AppCompatActivity() {

    private var editTextContext = ""
    lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private val searchRunnable = Runnable {
        search()
    }

    private val creator = Creator()
    private val trackInteractor = creator.provideTrackInteractor()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TrackAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyInteractor = creator.provideHistoryInteractor(
            getSharedPreferences(
                PrefKeys.PREFS, MODE_PRIVATE
            )
        )

        searchHistory = SearchHistory(
            historyInteractor = historyInteractor, binding = binding
        )
        trackAdapter = TrackAdapter { track ->
            historyInteractor.addToHistory(track)
            if (clickDebounce()) {
                val intent = Intent(this, TrackActivity::class.java)
                intent.putExtra(IntentConsts.TRACK.name, track)
                startActivity(intent)
            }
        }
        historyAdapter = trackAdapter
        binding.historyRecyclerView.adapter = trackAdapter
        binding.recyclerView.adapter = trackAdapter

        searchHistory.showList()
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                editTextContext = binding.editText.text.toString()
                if (binding.editText.hasFocus() && s?.isEmpty() == true) {
                    searchHistory.showList()
                    binding.progressBar.visibility = View.GONE
                } else searchHistory.hideHistoryViews()

                if (s?.isEmpty() == true) {
                    historyAdapter.saveData(searchHistory.getHistory())
                    binding.recyclerView.isVisible = false
                } else {
                    trackAdapter.saveData(emptyList())
                    binding.historyRecyclerView.isVisible = false
                }
                searchDebounce()
                deleteErrorViews()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        }
        binding.editText.addTextChangedListener(editTextWatcher)

        binding.clearIcon.setOnClickListener {
            binding.editText.text.clear()
            binding.editText.clearFocus()
            binding.recyclerView.isVisible = false
            searchHistory.showList()
            deleteErrorViews()
            hideKeyboard()
        }

        binding.updateButton.setOnClickListener {
            search()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editText.text.isEmpty()) {
                searchHistory.showList()
            }

        }

        binding.historyButton.setOnClickListener {
            searchHistory.clearHistory()
            searchHistory.hideHistoryViews()
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) = with(binding) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextContext = savedInstanceState.getString("EDIT_TEXT_CONTEXT", "")
        editText.setText(editTextContext)

    }

    private fun hideKeyboard() = with(binding) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun noInternetError() = with(binding) {
        updateButton.isVisible = true
        errorText.isVisible = true
        errorImage.isVisible = true
        errorText.setText(R.string.no_internet)
        errorImage.setImageResource(R.drawable.ic_no_internet)
        recyclerView.isVisible = false
        historyRecyclerView.isVisible = false
        progressBar.isVisible = false
    }

    private fun notFoundError() = with(binding) {
        errorText.isVisible = true
        errorImage.isVisible = true
        errorText.setText(R.string.not_found)
        errorImage.setImageResource(R.drawable.ic_not_found)
        recyclerView.isVisible = false
        historyRecyclerView.isVisible = false
        progressBar.isVisible = false
    }

    private fun deleteErrorViews() = with(binding) {
        errorLinear.isVisible = false
        errorText.isVisible = false
        errorImage.isVisible = false
        updateButton.isVisible = false
    }

    private fun search() = with(binding) {
        val request = editText.text.toString()
        if (request.isNotEmpty()) {
            deleteErrorViews()
            errorLinear.isVisible = true
            trackAdapter.saveData(emptyList())
            binding.progressBar.isVisible = true
            recyclerView.isVisible = true

            trackInteractor.searchTracks(
                request
            ) { foundTracks, resultCode ->
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (editText.text.toString() == request) {
                        when (resultCode) {
                            200 -> {
                                if (foundTracks.isNotEmpty()) {

                                    searchHistory.hideHistoryViews()
                                    trackAdapter.saveData(foundTracks)

                                } else notFoundError()

                            }

                            400 -> {
                                noInternetError()
                            }

                            else -> {
                                noInternetError()
                            }
                        }
                    }
                }
            }
        }
    }

    fun searchDebounce() {
        if (binding.editText.text.isNotBlank()) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        binding.progressBar.visibility = View.GONE
        if (binding.historyButton.visibility == View.VISIBLE) {
            historyAdapter.saveData(searchHistory.getHistory())
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

