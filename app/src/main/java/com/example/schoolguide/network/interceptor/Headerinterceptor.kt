package com.example.schoolguide.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class Headerinterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(originalRequest.method(), originalRequest.body())
        return chain.proceed(requestBuilder.build())
    }
}