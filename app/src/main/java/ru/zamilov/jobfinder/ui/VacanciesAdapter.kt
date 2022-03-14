package ru.zamilov.jobfinder.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.zamilov.jobfinder.model.Vacancy

class VacanciesAdapter : PagingDataAdapter<Vacancy, VacancyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VacancyViewHolder.create(parent)

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) =
        holder.bind(getItem(position))

    object DiffCallback : DiffUtil.ItemCallback<Vacancy>() {

        override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean =
            oldItem == newItem
    }
}