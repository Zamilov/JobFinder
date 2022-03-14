package ru.zamilov.jobfinder.api

import ru.zamilov.jobfinder.model.Vacancy

data class Response(
    val items: List<Vacancy>,
    val found: Int,
    val pages: Int,
    val per_page: Int,
    val page: Int,
    val clusters: Any?,
    val arguments: Any?,
    val alternate_url: String,
)
