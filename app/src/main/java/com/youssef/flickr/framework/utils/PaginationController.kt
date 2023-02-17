package com.youssef.flickr.framework.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youssef.flickr.framework.utils.Constants.Pagination
import javax.inject.Inject

class PaginationController @Inject constructor() : RecyclerView.OnScrollListener() {
    private var pageNumber: Int = Pagination.PAGE_NUMBER
    private var layoutManager: LinearLayoutManager? = null
    private var loadMoreItems: (pageNum: Int) -> Unit = {}

    private var isLoading = false
    fun isLoading(value: Boolean) {
        isLoading = value
    }

    private var isLastPage = false
    fun isLastPage(value: Boolean) {
        isLastPage = value
    }

    fun setLayoutManager(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
    }

    fun onLoadMoreItems(loadMoreItems: (pageNum: Int) -> Unit) {
        this.loadMoreItems = loadMoreItems
    }

    fun reload() {
        pageNumber = Pagination.PAGE_NUMBER
        loadNextPage()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        layoutManager?.let {
            val visibleItemCount = it.childCount
            val totalItemCount = it.itemCount
            val firstVisibleItemPosition = it.findFirstVisibleItemPosition()
            if (isLoading || isLastPage) {
                return
            }
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                pageNumber++
                loadNextPage()
            }
        }
    }

    private fun loadNextPage() {
        loadMoreItems.invoke(pageNumber)
    }

    fun clean() {
        layoutManager = null
    }
}