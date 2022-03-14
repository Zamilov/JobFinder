package ru.zamilov.jobfinder.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class VacanciesLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<VacanciesLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: VacanciesLoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): VacanciesLoadStateViewHolder {
        return VacanciesLoadStateViewHolder.create(parent, retry)
    }
}