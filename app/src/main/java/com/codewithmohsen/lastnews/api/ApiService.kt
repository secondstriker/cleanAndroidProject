package com.codewithmohsen.lastnews.api

import com.codewithmohsen.lastnews.models.ResponseModel
import retrofit2.http.*


interface ApiService {

    // for simplicity, we just have no arguments, next we add
    /**
     * fetch news
     */
    @GET("top-headlines?country=us&category=health&page=1&apikey=4803e6b0c58d42cead1a86a6727cb49b")
    suspend fun fetchNews(): NetworkResponse<ResponseModel, APIErrorResponse<ErrorModel>>
}