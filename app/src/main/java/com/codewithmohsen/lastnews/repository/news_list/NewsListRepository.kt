package com.codewithmohsen.lastnews.repository.news_list

import com.codewithmohsen.lastnews.models.Article
import com.codewithmohsen.lastnews.models.Category
import com.codewithmohsen.lastnews.repository.Resource
import kotlinx.coroutines.flow.Flow

interface NewsListRepository {

    suspend fun fetchNews(category: Category, page: Int)
    val news: Flow<Resource<List<Article>>>
}