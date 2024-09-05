package com.mahmoud.newsapp.api

import com.mahmoud.newsapp.models.NewsResponse
import com.mahmoud.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("pageSize") pageSize: Int = 30,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("pageSize") pageSize: Int = 30,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>
}