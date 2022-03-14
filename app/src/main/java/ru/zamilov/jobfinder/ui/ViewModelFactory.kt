package ru.zamilov.jobfinder.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ru.zamilov.jobfinder.data.HeadhunterRepository

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: HeadhunterRepository,
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        if (modelClass.isAssignableFrom(SearchVacanciesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchVacanciesViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}