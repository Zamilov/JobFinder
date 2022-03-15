package ru.zamilov.jobfinder.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.hh.ru/"

interface HeadhunterApiService {
    @GET("vacancies?area=2&search_period=30&per_page=20&order_by=publication_time")
    suspend fun searchVacancies(
        @Query("text") text: String,
        @Query("page") page: Int,
    ): Response

    companion object {
        fun create(): HeadhunterApiService =
            Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .add(KotlinJsonAdapterFactory())
                            .build()
                    )
                )
                .baseUrl(BASE_URL)
                .build()
                .create()
    }
}

