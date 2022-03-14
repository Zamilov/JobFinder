package ru.zamilov.jobfinder.model

import com.squareup.moshi.Json

data class Vacancy(
    val id: String,
    val premium: Boolean,
    val name: String,
    val department: Department?,
    val has_test: Boolean,
    val response_letter_required: Boolean,
    val area: Area,
    val salary: Salary?,
    val type: Type,
    val address: Any?,
    val response_url: Any?,
    val sort_point_distance: Any?,
    val published_at: String,
    val created_at: String,
    val archived: Boolean,
    val apply_alternate_url: String,
    val insider_interview: Any?,
    val url: String,
    val alternate_url: String,
    val relations: List<Any>,
    val employer: Employer,
    val snippet: Snippet,
    val contacts: Any?,
    val schedule: Schedule,
    val working_days: List<Any>,
    val working_time_intervals: List<Any>,
    val working_time_modes: List<Any>,
    val accept_temporary: Boolean,
)

data class Department(
    val id: String,
    val name: String,
)

data class Salary(
    val to: Int?,
    val from: Int?,
    val currency: String?,
    val gross: Boolean,
)

data class Area(
    val id: String,
    val name: String,
    val url: String,
)

data class Type(
    val id: String,
    val name: String,
)

data class Employer(
    val id: String,
    val name: String,
    val url: String,
    val alternate_url: String,
    val logo_urls: LogoUrls?,
    val vacancies_url: String,
    val trusted: Boolean,
)

data class LogoUrls(
    val original: String,
    @Json(name = "90") val small: String,
    @Json(name = "240") val middle: String,
)

data class Snippet(
    val requirement: String?,
    val responsibility: String?,
)

data class Schedule(
    val id: String,
    val name: String,
)