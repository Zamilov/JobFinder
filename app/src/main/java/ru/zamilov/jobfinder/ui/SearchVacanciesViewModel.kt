package ru.zamilov.jobfinder.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import ru.zamilov.jobfinder.data.HeadhunterRepository

class SearchVacanciesViewModel(
    private val repository: HeadhunterRepository,
    private val state: SavedStateHandle,
) : ViewModel() {

    companion object {
        const val KEY_SEARCH = "VACANCY"
        const val DEFAULT_SEARCH = "android developer"
    }

    init {
        if (!state.contains(KEY_SEARCH))
            state.set(KEY_SEARCH, DEFAULT_SEARCH)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow = state.getLiveData<String>(KEY_SEARCH)
        .asFlow()
        .flatMapLatest { repository.getSearchResultStream(it) }
        .cachedIn(viewModelScope)

    fun showVacancy(query: String) {
        state.set(KEY_SEARCH, query)
    }
}