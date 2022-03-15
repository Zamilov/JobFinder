package ru.zamilov.jobfinder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zamilov.jobfinder.api.HeadhunterApiService
import ru.zamilov.jobfinder.model.Vacancy

class HeadhunterRepository(private val service: HeadhunterApiService) {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    fun getSearchResultStream(query: String): Flow<PagingData<Vacancy>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { HeadhunterPagingSource(service, query) }
        ).flow
    }
}