package com.codewithmohsen.lastnews.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmohsen.lastnews.repository.news_list.NewsListRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsListViewModel constructor(
    private val repo: NewsListRepository
): ViewModel() {


    private val job = Job()

    fun fetchNews() {
        viewModelScope.launch(job) {
            repo.fetchNews()
        }
    }

    fun getNewsAsFlow() = repo.news

    fun cancel() {
        job.cancel()
    }
}