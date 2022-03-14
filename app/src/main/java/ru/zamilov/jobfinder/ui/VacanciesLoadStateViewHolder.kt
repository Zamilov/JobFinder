package ru.zamilov.jobfinder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.zamilov.jobfinder.R
import ru.zamilov.jobfinder.databinding.FooterViewItemBinding

class VacanciesLoadStateViewHolder(
    private val binding: FooterViewItemBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): VacanciesLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_view_item, parent, false)
            val binding = FooterViewItemBinding.bind(view)
            return VacanciesLoadStateViewHolder(binding, retry)
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }
}