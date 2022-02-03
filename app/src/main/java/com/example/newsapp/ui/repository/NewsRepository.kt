package com.example.newsapp.ui.repository

import com.example.newsapp.ui.api.RetrofitInstance
import com.example.newsapp.ui.db.ArticleDatabase
import com.example.newsapp.ui.models.Article
import retrofit2.Retrofit

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) =
        db.getArticleDao().upsert(article)

    fun getSavedNewsLive() =
        db.getArticleDao().getAllArticlesLive()

    suspend fun getSavedNews() =
        db.getArticleDao().getAllArticles()

    suspend fun deleteArticles(article: Article) =
        db.getArticleDao().deleteArticles(article)
}