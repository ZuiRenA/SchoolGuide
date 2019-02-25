package com.example.schoolguide.network.interceptor

import com.example.schoolguide.network.exception.ConnectionException
import com.example.schoolguide.network.exception.ResultInvalidException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Exception

class HttpIntercepter: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse: Response
        try {
            originalResponse = chain.proceed(request);
        } catch (e: Exception) {
            throw ConnectionException()
        }

        if (originalResponse.code() != 200) {
            throw ResultInvalidException()
        }

        val source = originalResponse.body()?.source()
        source!!.request(Long.MAX_VALUE)
        val byteString = source.buffer().snapshot().utf8()
        val responseBody = ResponseBody.create(null, byteString)
        return originalResponse.newBuilder().body(responseBody).build()
    }
}