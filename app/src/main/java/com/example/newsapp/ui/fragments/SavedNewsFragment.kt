package com.example.newsapp.ui.fragments

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.ui.NewsActivity
import com.example.newsapp.ui.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment: Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Статья успешно удалена", Snackbar.LENGTH_LONG).apply {
                    setAction("Отмена") {
                        viewModel.saveArticleLive(article)
                    }
                    show()
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (viewHolder != null) {
                    val foregroundView: View =
                        (viewHolder as NewsAdapter.ArticleViewsHolder).viewForeground
                    getDefaultUIUtil().onSelected(foregroundView)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (dX < 0F) {
                    (viewHolder as NewsAdapter.ArticleViewsHolder).showRadius()
                } else {
                    (viewHolder as NewsAdapter.ArticleViewsHolder).hideRadius()
                }
                if (dX < -60F) {
                    (viewHolder as NewsAdapter.ArticleViewsHolder).hideStripe()
                } else {
                    (viewHolder as NewsAdapter.ArticleViewsHolder).showStripe()
                }
                val foregroundView: View =
                    (viewHolder as NewsAdapter.ArticleViewsHolder).viewForeground
                getDefaultUIUtil().onDraw(
                    c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive
                )
            }

            override fun onChildDrawOver(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val foregroundView: View =
                    (viewHolder as NewsAdapter.ArticleViewsHolder).viewForeground
                getDefaultUIUtil().onDrawOver(
                    c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive
                )
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                (viewHolder as NewsAdapter.ArticleViewsHolder).hideRadius()
                (viewHolder as NewsAdapter.ArticleViewsHolder).showStripe()
                val foregroundView: View =
                    (viewHolder as NewsAdapter.ArticleViewsHolder).viewForeground
                getDefaultUIUtil().clearView(foregroundView)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSavedNews)

        viewModel.getSavedNewsLive().observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isEmpty()) {
                showStartTextOnSaveNews()
            } else {
                hideStartTextOnSaveNews()
            }
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun hideStartTextOnSaveNews() {
        startTextOnSaveNews.visibility = View.INVISIBLE
    }

    private fun showStartTextOnSaveNews() {
        startTextOnSaveNews.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}