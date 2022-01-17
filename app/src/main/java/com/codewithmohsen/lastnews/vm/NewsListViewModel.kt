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
): ViewModel() {


    private val job = Job()

    fun fetchNews() = fetchNews(Category.general, 1)

    fun fetchNews(category: Category, page: Int) {
        viewModelScope.launch(job) {
            repo.fetchNews(category, page)
        }
    }

    fun getNewsAsFlow() = repo.news

    fun cancel() {
        job.cancel()
    }
}