package com.example.playlistmaker.ui.activities

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
import com.example.playlistmaker.API.ITunesApi
import com.example.playlistmaker.API.responces.TrackResponce
import com.example.playlistmaker.IntentConsts
import com.example.playlistmaker.R
import com.example.playlistmaker.adapters.track.TrackAdapter
import com.example.playlistmaker.data.track.prefs.PrefKeys
import com.example.playlistmaker.data.track.prefs.historyprefs.HistoryPrefs
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.utils.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindActivity : AppCompatActivity() {

    private var editTextContext = ""
    private var baseUrl = "https://itunes.apple.com"
    lateinit var binding: ActivityFindBinding
    private val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val iTunesApiService = retrofit.create(ITunesApi::class.java)
    private lateinit var trackAdapter: TrackAdapter
    private val searchRunnable = Runnable {
        search()
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchHistory = SearchHistory(
            historyPrefs = HistoryPrefs(
                getSharedPreferences(
                    PrefKeys.PREFS, MODE_PRIVATE
                )
            ), binding = binding
        )
        trackAdapter = TrackAdapter { track ->
            val historyPrefs = HistoryPrefs(
                getSharedPreferences(
                    PrefKeys.PREFS, MODE_PRIVATE
                )
            )
            historyPrefs.addToHistoryList(track)
            if (clickDebounce()) {
                val intent = Intent(this, TrackActivity::class.java)
                intent.putExtra(IntentConsts.TRACK, track)
                startActivity(intent)
            }
        }
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
                    val historyPrefs = HistoryPrefs(
                        getSharedPreferences(
                            PrefKeys.PREFS, MODE_PRIVATE
                        )
                    )

                    trackAdapter.saveData(historyPrefs.getHistoryList())
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
        errorText.setText(R.string.no_internet)
        errorImage.setImageResource(R.drawable.ic_no_internet)
        recyclerView.visibility = View.GONE
    }

    private fun notFoundError() = with(binding) {
        errorLinear.visibility = View.VISIBLE
        errorText.setText(R.string.not_found)
        errorImage.setImageResource(R.drawable.ic_not_found)
        recyclerView.visibility = View.GONE
    }

    private fun deleteErrorViews() = with(binding) {
        errorLinear.visibility = View.GONE
        updateButton.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun search() = with(binding) {
        deleteErrorViews()
        if (editText.text.isNotBlank()) {
            trackAdapter.saveData(emptyList())
            binding.progressBar.visibility = View.VISIBLE
            iTunesApiService.search("${editText.text}").enqueue(object : Callback<TrackResponce> {
                override fun onResponse(
                    call: Call<TrackResponce>, response: Response<TrackResponce>
                ) {
                    progressBar.visibility = View.GONE
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                if (editText.text.isNotBlank()) trackAdapter.saveData(
                                    ArrayList(
                                        response.body()?.results!!
                                    )
                                )
                            } else {
                                notFoundError()
                            }
                        }

                        else -> noInternetError()
                    }
                }

                override fun onFailure(call: Call<TrackResponce>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    noInternetError()
                }

            })
        }
    }

    fun searchDebounce() {
        if (binding.editText.text.isNotBlank()) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    fun clickDebounce(): Boolean {
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
            val historyPrefs = HistoryPrefs(
                getSharedPreferences(
                    PrefKeys.PREFS, MODE_PRIVATE
                )
            )
            trackAdapter.saveData(historyPrefs.getHistoryList())
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}


