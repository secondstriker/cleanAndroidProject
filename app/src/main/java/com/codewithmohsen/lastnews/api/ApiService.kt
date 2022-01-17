package com.codewithmohsen.lastnews.api

import com.codewithmohsen.lastnews.api.Constants.COUNTRY
import com.codewithmohsen.lastnews.api.Constants.PAGE_SIZE
import com.codewithmohsen.lastnews.models.ResponseModel
import retrofit2.http.*


interface ApiService {

    /**
     * fetch news
     */
    @Headers("X-Api-Key:2af6a4552d8c48a6a126cf6c876a3795")
    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("country") country: String = COUNTRY,
        @Query("category") category: String,
        @Query("page") page: Int
    ): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>>

}