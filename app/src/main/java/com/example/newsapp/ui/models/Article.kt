package com.example.newsapp.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newsapp.ui.db.Converters
import com.example.newsapp.ui.models.Source
import java.io.Serializable

@Entity(tableName = "articles")
@TypeConverters(Converters::class)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    var source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable