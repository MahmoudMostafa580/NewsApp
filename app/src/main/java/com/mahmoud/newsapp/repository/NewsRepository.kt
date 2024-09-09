package com.mahmoud.newsapp.repository

import com.mahmoud.newsapp.api.RetrofitInstance
import com.mahmoud.newsapp.database.ArticlesDatabase

class NewsRepository(
    db: ArticlesDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsApi.getBreakingNews(countryCode, pageNumber)


}
