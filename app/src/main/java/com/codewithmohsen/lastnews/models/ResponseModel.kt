package com.codewithmohsen.lastnews.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseModel(
    //general
    @Json(name = "status")
    val status: String?,
    //when we succeed
    @Json(name = "articles")
    val articles: List<Article>?,
    @Json(name = "totalResults")
    val totalResults: Int?,
    //when we have error
    @Json(name = "code")
    val code: String?,
    @Json(name = "message")
    val message: String?
)