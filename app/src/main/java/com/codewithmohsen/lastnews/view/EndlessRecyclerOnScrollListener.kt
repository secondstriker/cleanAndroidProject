package com.codewithmohsen.lastnews.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


abstract class EndlessRecyclerOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int
) :
    RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        Timber.d("beforeLoading $loading $totalItemCount $previousTotal")
        if (loading) {
            if (totalItemCount > previousTotal || previousTotal > totalItemCount) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        Timber.d("afterLoading $loading $totalItemCount $previousTotal")
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()

}