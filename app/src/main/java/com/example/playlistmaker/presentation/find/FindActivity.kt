package com.example.playlistmaker.presentation.find

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.domain.prefs.PrefKeys
import com.example.playlistmaker.presentation.SearchHistory
import com.example.playlistmaker.presentation.track.TrackActivity
import com.example.playlistmaker.utils.consts.IntentConsts
import com.example.playlistmaker.utils.creator.Creator

class FindActivity : AppCompatActivity() {

    private var editTextContext = ""
    lateinit var binding: ActivityFindBinding
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
        binding = ActivityFindBinding.inflate(layoutInflater)
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
                } else trackAdapter.saveData(emptyList())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        }
        binding.editText.addTextChangedListener(editTextWatcher)
        binding.clearIcon.setOnClickListener {
            binding.editText.text.clear()
            binding.editText.clearFocus()
            searchHistory.showList()
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
        errorLinear.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        errorImage.visibility = View.VISIBLE
        errorText.setText(R.string.no_internet)
        errorImage.setImageResource(R.drawable.ic_no_internet)
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun notFoundError() = with(binding) {
        errorText.visibility = View.VISIBLE
        errorImage.visibility = View.VISIBLE
        errorLinear.visibility = View.VISIBLE
        errorText.setText(R.string.not_found)
        errorImage.setImageResource(R.drawable.ic_not_found)
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun deleteErrorViews() = with(binding) {
        errorLinear.visibility = View.GONE
        updateButton.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun search() = with(binding) {
        deleteErrorViews()
        trackAdapter.saveData(emptyList())
        binding.progressBar.visibility = View.VISIBLE
        if (isNetworkAvailable(this@FindActivity)) trackInteractor.searchTracks(
            editText.text.toString(),
            object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>, resultCode: Int) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        when (resultCode) {
                            200 -> {
                                if (foundTracks.isNotEmpty()) {
                                    searchHistory.hideHistoryViews()
                                    trackAdapter.saveData(foundTracks)
                                } else {
                                    notFoundError()
                                }
                            }

                            else -> {
                                noInternetError()
                            }
                        }
                    }
                }
            })
        else {
            progressBar.visibility = View.GONE
            noInternetError()
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

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}


