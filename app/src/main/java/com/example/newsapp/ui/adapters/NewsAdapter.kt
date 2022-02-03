package com.example.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.ui.models.Article
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewsHolder>() {
    inner class ArticleViewsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val viewBackground = itemView.view_background
        val viewForeground = itemView.view_foreground

        fun hideRadius() {
            viewForeground.radius = 0F
        }

        fun showRadius() {
            viewForeground.radius = 60F
        }

        fun showStripe() {
            viewBackground.stripe.visibility = View.VISIBLE
        }

        fun hideStripe() {
            viewBackground.stripe.visibility = View.INVISIBLE
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewsHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        return ArticleViewsHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewsHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = customDate(article.publishedAt)
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    private fun customDate(date: String?): String {
        if (date == null) {
            return "Дата не определена"
        }
        val year = date.subSequence(0, 4).toString()
        return date
            .drop(5)
            .replace("T", ".$year  ")
            .replace("-", ".")
            .dropLast(4)
    }
}