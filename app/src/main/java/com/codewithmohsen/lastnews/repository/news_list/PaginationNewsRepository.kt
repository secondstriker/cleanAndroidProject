package com.codewithmohsen.lastnews.repository.news_list

import com.codewithmohsen.lastnews.di.CoroutinesScopesModule.ApplicationScope
import com.codewithmohsen.lastnews.di.IoDispatcher
import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.models.ResponseModel
import com.codewithmohsen.lastnews.repository.BaseOnlineRepository
import com.codewithmohsen.lastnews.repository.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

abstract class PaginationNewsRepository(
    @ApplicationScope private val externalCoroutineScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseOnlineRepository<ResponseModel, List<Article>>(externalCoroutineScope, ioDispatcher) {

    var page: Int = 1
        private set

    override suspend fun bodyToResult(apiModel: ResponseModel?): List<Article> {
        val preResult = super.getResultAsFlow().value
        page++

        Timber.d("PaginationRepository")

        val result = if (!apiModel?.articles.isNullOrEmpty() && !preResult.data.isNullOrEmpty())
            preResult.data.plus(apiModel?.articles!!)
        else if (preResult.data.isNullOrEmpty())
            apiModel?.articles
        else preResult.data

        if (result.isNullOrEmpty())
            page = 1

        return result ?: mutableListOf()
    }

    protected fun reset() {
        page = 1
    }
}