package com.codewithmohsen.lastnews.repository.news_list

import com.codewithmohsen.lastnews.api.APIErrorResponse
import com.codewithmohsen.lastnews.api.ApiService
import com.codewithmohsen.lastnews.api.ErrorModel
import com.codewithmohsen.lastnews.api.NetworkResponse
import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.models.ResponseModel
import com.codewithmohsen.lastnews.repository.BaseOnlineRepository
import com.codewithmohsen.lastnews.repository.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class NewsListRepositoryImpl constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val externalCoroutineDispatcher: CoroutineScope
): BaseOnlineRepository<ResponseModel, List<Article>>(externalCoroutineDispatcher, ioDispatcher),
    NewsListRepository {

    override suspend fun fetchNews() = withContext(ioDispatcher) {
        super.fetch()
    }

    override val news: Flow<Resource<List<Article>>>
        get() = super.getResultAsFlow()

    override suspend fun apiCall(): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>> =
        apiService.fetchNews()

    override suspend fun bodyToResult(apiModel: ResponseModel?): List<Article> =
        apiModel?.articles ?: mutableListOf()
}