package com.mahmoud.newsapp.repository

import com.mahmoud.newsapp.api.RetrofitInstance

class NewsRepository(
//    db: ArticlesDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.newsApi.searchNews(searchQuery, pageNumber)


}
