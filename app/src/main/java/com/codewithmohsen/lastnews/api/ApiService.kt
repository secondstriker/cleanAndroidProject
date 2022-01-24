package com.codewithmohsen.lastnews.api

import com.codewithmohsen.lastnews.api.Constants.PAGE_SIZE
import com.codewithmohsen.lastnews.models.ResponseModel
import retrofit2.http.*


interface ApiService {

    /**
     * fetch news
     */
    @Headers("X-Api-Key:${Constants.API_KEY}")
    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("category") category: String,
        @Query("page") page: Int
    ): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>>

}