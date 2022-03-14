package ru.zamilov.jobfinder

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import ru.zamilov.jobfinder.api.HeadhunterApiService
import ru.zamilov.jobfinder.data.HeadhunterRepository
import ru.zamilov.jobfinder.ui.ViewModelFactory

object Injection {
    private fun provideHeadhunterRepository(): HeadhunterRepository {
        return HeadhunterRepository(HeadhunterApiService.create())
    }

    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideHeadhunterRepository())
    }
}