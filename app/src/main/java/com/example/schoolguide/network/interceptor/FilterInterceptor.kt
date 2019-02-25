package com.example.schoolguide.network.interceptor

import android.text.TextUtils
import com.example.schoolguide.network.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

class FilterInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder = originalRequest.url().newBuilder()
        val headers = originalRequest.headers()
        if (headers != null && headers.size() > 0) {
            val requestType = headers.get(HttpConfig.HTTP_REQUEST_TYPE_KEY)
            if (!TextUtils.isEmpty(requestType)) {
                when (requestType) {
                    HttpConfig.HTTP_REQUEST_WEATHER -> {
                        httpBuilder.addQueryParameter(HttpConfig.KEY, HttpConfig.KEY_WEATHER)
                    }
                    HttpConfig.HTTP_REQUEST_QR_CODE -> {
                        httpBuilder.addQueryParameter(HttpConfig.KEY, HttpConfig.KEY_QR_CODE)
                    }
                    HttpConfig.HTTP_REQUEST_NEWS -> {
                        httpBuilder.addQueryParameter(HttpConfig.KEY, HttpConfig.KEY_NEWS)
                    }
                }
            }
        }
        val requestBuilder = originalRequest.newBuilder()
            .removeHeader(HttpConfig.HTTP_REQUEST_TYPE_KEY)
            .url(httpBuilder.build())
        return chain.proceed(requestBuilder.build())
    }
}
