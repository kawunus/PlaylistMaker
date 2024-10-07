package com.example.playlistmaker.presentation.search.activity

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
    lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter


    private lateinit var viewModel: SearchViewModel

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var historyAdapter: TrackAdapter

    private var currentHistory: List<Track> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            when (state) {
                is SearchState.Content -> {
                    showContent()
                }

                SearchState.Empty -> {
                    notFoundError()
                }

                SearchState.Error -> {
                    noInternetError()
                }

                SearchState.History -> {
                    showHistory()
                }

                SearchState.Loading -> {
                    showLoading()
                }
            }
        }

        trackAdapter = TrackAdapter { track ->
            viewModel.addToHistory(track)
            if (clickDebounce()) {
                val intent = Intent(this, TrackActivity::class.java)
                intent.putExtra(IntentConsts.TRACK.name, track)
                startActivity(intent)
            }
        }

        historyAdapter = trackAdapter
        binding.historyRecyclerView.adapter = trackAdapter
        binding.recyclerView.adapter = trackAdapter

        viewModel.observeHistory().observe(this) { historyList ->
            historyAdapter.saveData(historyList)
            currentHistory = historyList
        }

        viewModel.observeTrackList().observe(this) { trackList ->
            trackAdapter.saveData(trackList)
        }

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
                    viewModel.showHistory()
                    binding.progressBar.visibility = View.GONE
                } else hideHistory()

                if (s?.isEmpty() == true) {
                    viewModel.getHistory()
                    binding.recyclerView.isVisible = false
                } else {
                    trackAdapter.saveData(emptyList())
                    binding.historyRecyclerView.isVisible = false
                }
                viewModel.searchDebounce(binding.editText.text.toString())
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
            viewModel.showHistory()
            deleteErrorViews()
            hideKeyboard()
        }

        binding.updateButton.setOnClickListener {
            viewModel.search(binding.editText.text.toString())
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editText.text.isEmpty()) {
                showHistory()
            }
        }

        binding.historyButton.setOnClickListener {
            viewModel.clearHistory()
            hideHistory()
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
    }

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    // Функции для состояний экрана

    private fun showLoading() = with(binding) {
        hideHistory()
        deleteErrorViews()
        errorLinear.isVisible = true
        trackAdapter.saveData(emptyList())
        binding.progressBar.isVisible = true
        recyclerView.isVisible = true
    }

    private fun noInternetError() = with(binding) {
        hideHistory()
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
        hideHistory()
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

    private fun showHistory() = with(binding) {
        deleteErrorViews()
        recyclerView.isVisible = false
        if (currentHistory.isNotEmpty()) {
            historyButton.isVisible = true
            historyTextView.isVisible = true
            errorLinear.isVisible = false
            historyRecyclerView.isVisible = true
        } else hideHistory()
    }

    private fun hideHistory() = with(binding) {
        historyRecyclerView.isVisible = false
        historyButton.isVisible = false
        historyTextView.isVisible = false
    }

    private fun showContent() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = true
        hideHistory()
        deleteErrorViews()
    }

    // TODO: Ошибка при быстром вводе
    // TODO: Не показывается история при входе
}
