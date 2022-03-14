package ru.zamilov.jobfinder.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.zamilov.jobfinder.R
import ru.zamilov.jobfinder.databinding.VacancyCardBinding
import ru.zamilov.jobfinder.model.Vacancy
import ru.zamilov.jobfinder.util.formatText
import java.util.*

class VacancyViewHolder(private val binding: VacancyCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var vacancy: Vacancy? = null

    init {
        binding.root.setOnClickListener {
            vacancy?.alternate_url.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): VacancyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.vacancy_card, parent, false)
            return VacancyViewHolder(VacancyCardBinding.bind(view))
        }
    }

    fun bind(vacancy: Vacancy?) {
        this.vacancy = vacancy

        if (vacancy != null) {
            binding.vacancyName.text = vacancy.name
            if (vacancy.employer.logo_urls != null) {
                val imgUrl = vacancy.employer.logo_urls.middle.toUri()
                    .buildUpon()
                    .scheme("https")
                    .build()
                Glide.with(binding.employerLogo.context)
                    .load(imgUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_img)
                    )
                    .into(binding.employerLogo)
            }

            if (vacancy.salary != null) {
                when {
                    vacancy.salary.from == null -> binding.salary.text =
                        String.format(Locale("RU"),
                            "до %,d ${vacancy.salary.currency}",
                            vacancy.salary.to)
                    vacancy.salary.to == null -> binding.salary.text =
                        String.format(Locale("RU"),
                            "от %,d ${vacancy.salary.currency}",
                            vacancy.salary.from)
                    else -> binding.salary.text =
                        String.format(Locale("RU"),
                            "%,d - %,d ${vacancy.salary.currency}",
                            vacancy.salary.from, vacancy.salary.to)
                }
            }

            if (vacancy.snippet.requirement != null)
                binding.requirement.text = formatText(vacancy.snippet.requirement)

            if (vacancy.snippet.responsibility != null)
                binding.responsibility.text = formatText(vacancy.snippet.responsibility)
        }
    }
}