package com.mahmoud.newsapp.repository

import com.mahmoud.newsapp.api.RetrofitInstance
import com.mahmoud.newsapp.database.ArticleDao
import com.mahmoud.newsapp.database.ArticlesDatabase
import com.mahmoud.newsapp.models.Article

class NewsRepository(
    val db: ArticlesDatabase
) {

    //Retrofit methods
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.newsApi.searchNews(searchQuery, pageNumber)


    //Room methods
    suspend fun upsert(article: Article) = db.getArticleDao().insertArticle(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}
