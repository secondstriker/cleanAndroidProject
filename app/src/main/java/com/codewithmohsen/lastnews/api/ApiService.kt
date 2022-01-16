package com.codewithmohsen.lastnews.api

import com.codewithmohsen.lastnews.models.ResponseModel
import retrofit2.http.*


interface ApiService {

    // for simplicity, we just have no arguments, next we add
    /**
     * fetch news
     */
    @GET("top-headlines?country=us&category=general&page=1")
    suspend fun fetchNews(): NetworkResponse<ResponseModel, APIErrorResponse<ErrorBody>>
}