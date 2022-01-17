package com.codewithmohsen.lastnews.repository.news_list

import com.codewithmohsen.lastnews.api.APIErrorResponse
import com.codewithmohsen.lastnews.api.ApiService
import com.codewithmohsen.lastnews.api.ErrorModel
import com.codewithmohsen.lastnews.api.NetworkResponse
import com.codewithmohsen.lastnews.di.CoroutinesScopesModule.ApplicationScope
import com.codewithmohsen.lastnews.di.IoDispatcher
import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.models.ResponseModel
import com.codewithmohsen.lastnews.repository.BaseOnlineRepository
import com.codewithmohsen.lastnews.repository.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationScope private val externalCoroutineDispatcher: CoroutineScope
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