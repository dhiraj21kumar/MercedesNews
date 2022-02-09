package com.example.mercedesnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mercedesnews.R
import com.example.mercedesnews.adapters.NewsAdapter
import com.example.mercedesnews.ui.Component
import com.example.mercedesnews.ui.GuestNewsActivity
import com.example.mercedesnews.ui.NewsViewModel
import com.example.mercedesnews.util.Constants
import com.example.mercedesnews.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_guest_top_news.*
import kotlinx.android.synthetic.main.fragment_top_news.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuestTopNewsFragment: Fragment(R.layout.fragment_guest_top_news) {

//    lateinit var viewModel: NewsViewModel
val newsViewModel: NewsViewModel by viewModel()
    private val component = Component()
//    lateinit var newsAdapter: NewsAdapter

    val TAG = "TopNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = (activity as GuestNewsActivity).viewModel
        component.newsModel
        setupRecyclerView()

        component.newAdapterModel.setOnItemClickListner {
            Snackbar.make(view,"Please Login/Register", Snackbar.LENGTH_SHORT).show()
        }

        component.newsModel.topNews.observe(viewLifecycleOwner, Observer { response ->

            when (response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?. let { newsResponse ->
                        component.newAdapterModel.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = component.newsModel.topNewsPage == totalPages
                        if (isLastPage) {
                            rvTopNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->

                        Log.e(TAG, "An Error occured: $message")

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    private fun hideProgressBar() {
        guestPaginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        guestPaginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListner = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem
                    && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                component.newsModel.getTopNews("in")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
//        newsAdapter = NewsAdapter(get())
        component.newAdapterModel
        rvGuestTopNews.apply {
            component.newAdapterModel
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@GuestTopNewsFragment.scrollListner )
        }
    }
}