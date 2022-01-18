package com.codewithmohsen.lastnews.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.repository.news_list.NewsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repo: NewsListRepository
) : ViewModel() {

    private lateinit var job: Job

    private val selectedCategory = MutableLiveData<Int>()
    fun getSelectedCategory(): LiveData<Int> = selectedCategory
    fun setSelectedCategory(category: Int) {
        selectedCategory.value = category
    }

    fun fetchMoreNews() {
        newJob()
        viewModelScope.launch(job) {
            repo.fetchMoreNews()
        }
    }

    fun fetchNews(category: Category) {
        newJob()
        repo.setCategory(category)
        viewModelScope.launch(job) {
            repo.refresh()
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
        Timber.d("viewModel cancel")
    }

    private fun newJob() {
        if(!this::job.isInitialized)
            job = Job()
        if (job.isActive || job.isCancelled) {
            job.cancel()
            job = Job()
            job.invokeOnCompletion {
                Timber.d("viewModel job completed. is cancelled? ${job.isCancelled}")
            }
        }
    }

}