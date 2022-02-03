package com.example.newsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.ui.models.Article




@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticlesLive(): LiveData<List<Article>>

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<Article>

    @Delete
    suspend fun deleteArticles(article: Article)
}