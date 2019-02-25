package com.example.schoolguide.network

import com.example.schoolguide.network.interceptor.FilterInterceptor
import com.example.schoolguide.network.interceptor.Headerinterceptor
import com.example.schoolguide.network.interceptor.HttpIntercepter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object RetrofitManagement {
    private val READ_TIMEOUT: Long = 6000

    private val WRITE_TIMEOUT: Long = 6000

    private val CONNECT_TIMEOUT: Long = 6000

    private val serviceMap: Map<String, Any> = ConcurrentHashMap()

    private fun createaRetrofit(url: String): Retrofit {
        val builder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpIntercepter())
            .addInterceptor(Headerinterceptor())
            .addInterceptor(FilterInterceptor())
            .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level  = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)
        }

        val client = builder.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}