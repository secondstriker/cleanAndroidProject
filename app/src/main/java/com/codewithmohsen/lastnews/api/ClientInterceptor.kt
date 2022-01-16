package com.codewithmohsen.lastnews.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ClientInterceptor: Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request = chain.request()
        val url: HttpUrl =
            request.url.newBuilder().build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}