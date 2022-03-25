package ru.zamilov.jobfinder.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import kotlinx.coroutines.flow.collectLatest
import ru.zamilov.jobfinder.Injection
import ru.zamilov.jobfinder.R
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
        setSupportActionBar(binding.toolBar)

        binding.vacanciesList.adapter = adapter.withLoadStateFooter(
            VacanciesLoadStateAdapter { adapter.retry() }
        )

        lifecycleScope.launchWhenCreated {
            viewModel.pagingDataFlow.collectLatest {
                adapter.submitData(it)
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

                binding.swipeRefresh.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
            }
        }

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        binding.retryButton.setOnClickListener { adapter.retry() }

        binding.vacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.showVacancy(it)

                    binding.vacanciesList.scrollToPosition(0)
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.app_bar_filter -> {
                FilterFragment().show(supportFragmentManager, "filter")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        binding.vacanciesList.requestFocus()
    }
}