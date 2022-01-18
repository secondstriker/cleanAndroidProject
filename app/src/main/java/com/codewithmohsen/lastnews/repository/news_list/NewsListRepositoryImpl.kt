package com.codewithmohsen.lastnews.repository.news_list

import com.codewithmohsen.lastnews.api.APIErrorResponse
import com.codewithmohsen.lastnews.api.ApiService
import com.codewithmohsen.lastnews.api.ErrorModel
import com.codewithmohsen.lastnews.api.NetworkResponse
import com.codewithmohsen.lastnews.di.CoroutinesScopesModule.ApplicationScope
import com.codewithmohsen.lastnews.di.IoDispatcher
import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.models.ResponseModel
import com.codewithmohsen.lastnews.repository.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationScope private val externalCoroutineDispatcher: CoroutineScope
): PaginationNewsRepository(externalCoroutineDispatcher, ioDispatcher),
    NewsListRepository {

    private lateinit var category: Category

    override suspend fun fetchMoreNews() { super.fetch(false) }
    override fun setCategory(category: Category) { this.category = category }
    override suspend fun refresh() {
        super.reset()
        super.fetch(true)
    }

    override val news: Flow<Resource<List<Article>>>
        get() = super.getResultAsFlow()

    override suspend fun apiCall(): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>> =
        apiService.fetchNews(category = category.name, page = super.page)
}