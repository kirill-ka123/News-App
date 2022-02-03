package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.ui.NewsActivity
import com.example.newsapp.ui.ui.NewsViewModel
import com.example.newsapp.ui.util.Constants.Companion.LANGUAGE_FOR_BREAKING_NEWS
import com.example.newsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import androidx.core.content.ContextCompat

import android.view.WindowManager

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner


class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.breakingNewsPage = 1
            viewModel.breakingNewsResponse = null
            isLoading = true
            viewModel.getBreakingNews(LANGUAGE_FOR_BREAKING_NEWS)
        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    if (swipeRefreshLayout.isRefreshing) {
                        hideRefreshing()
                    }
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    if (swipeRefreshLayout.isRefreshing) {
                        hideRefreshing()
                    }
                    response.message?.let { message ->
                        Toast.makeText(activity, "Произошла ошибка: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    if (!swipeRefreshLayout.isRefreshing) {
                        showProgressBar()
                    }
                }
            }
        } )
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun hideRefreshing() {
        swipeRefreshLayout.isRefreshing = false
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private var scrollingListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndIsNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThenVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThenVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews(LANGUAGE_FOR_BREAKING_NEWS)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollingListener)
        }
    }
}