package com.codewithmohsen.lastnews.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmohsen.lastnews.repository.news_list.NewsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
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