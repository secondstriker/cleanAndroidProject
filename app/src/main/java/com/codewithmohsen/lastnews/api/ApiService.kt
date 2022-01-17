package com.codewithmohsen.lastnews.api

import com.codewithmohsen.lastnews.api.Constants.COUNTRY
import com.codewithmohsen.lastnews.api.Constants.PAGE_SIZE
import com.codewithmohsen.lastnews.models.ResponseModel
import retrofit2.http.*


interface ApiService {

    /**
     * fetch news
     */
    @Headers("X-Api-Key:4803e6b0c58d42cead1a86a6727cb49b")
    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("country") country: String = COUNTRY,
        @Query("category") category: String,
        @Query("page") page: Int
    ): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>>

}