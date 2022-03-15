package ru.zamilov.jobfinder.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import kotlinx.coroutines.flow.collectLatest
import ru.zamilov.jobfinder.Injection
import ru.zamilov.jobfinder.databinding.ActivitySearchVacanciesBinding

class SearchVacanciesActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchVacanciesViewModel
    private lateinit var binding: ActivitySearchVacanciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = VacanciesAdapter()
        viewModel = ViewModelProvider(this,
            Injection.provideViewModelFactory(this))[SearchVacanciesViewModel::class.java]

        binding = ActivitySearchVacanciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vacanciesList.adapter = adapter.withLoadStateFooter(
            VacanciesLoadStateAdapter { adapter.retry() }
        )

        lifecycleScope.launchWhenCreated {
            viewModel.pagingDataFlow.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefresh.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyList.isVisible = isListEmpty
                binding.vacanciesList.isVisible = !isListEmpty
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        binding.retryButton.setOnClickListener { adapter.retry() }

        binding.inputSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                updateVacancyListFromInput()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateVacancyListFromInput()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.vacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        binding.vacanciesList.requestFocus()
    }

    private fun updateVacancyListFromInput() {
        binding.inputSearch.text.trim().let {
            if (it.isNotBlank()) {
                viewModel.showVacancy(it.toString())
                binding.vacanciesList.scrollToPosition(0)
            }
        }
    }
}