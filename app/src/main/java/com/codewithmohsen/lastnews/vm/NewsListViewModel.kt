package com.codewithmohsen.lastnews.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.repository.news_list.NewsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repo: NewsListRepository
) : ViewModel() {


    private var job: Job = Job()

    fun fetchNews() {
        newJob()
        viewModelScope.launch(job) {
            repo.fetchMoreNews()
        }
    }

    fun fetchNews(category: Category) {
        newJob()
        repo.setCategory(category)
        viewModelScope.launch(job) {
            repo.fetchMoreNews()
        }
    }

    fun refresh() {
        newJob()
        viewModelScope.launch(job) {
            repo.refresh()
        }
    }

    fun getNewsAsFlow() = repo.news

    fun cancel() {
        job.cancel()
    }

    private fun newJob() {
        if (job.isActive) {
            job.cancel()
            job = Job()
        }
    }

}