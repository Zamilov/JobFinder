package ru.zamilov.jobfinder.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.zamilov.jobfinder.api.HeadhunterApiService
import ru.zamilov.jobfinder.model.Vacancy
import java.io.IOException

class HeadhunterPagingSource(
    private val backend: HeadhunterApiService,
    private val query: String,
) : PagingSource<Int, Vacancy>() {

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 0
            val response = backend.searchVacancies(query, nextPageNumber)

            return LoadResult.Page(
                data = response.items,
                prevKey = null, // Only paging forward.
                nextKey = response.page.plus(1)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}