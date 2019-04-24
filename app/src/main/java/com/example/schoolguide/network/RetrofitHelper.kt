package com.example.schoolguide.network

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import com.example.schoolguide.App
import com.example.schoolguide.network.NetConfig.BASE_URL
import com.example.schoolguide.network.NetConfig.FORMAT_KEY
import com.example.schoolguide.network.NetConfig.FORMAT_VALUE
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val Time_Out = 10L
    private fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(Time_Out, TimeUnit.SECONDS)
            .writeTimeout(Time_Out, TimeUnit.SECONDS)
            .connectTimeout(Time_Out, TimeUnit.SECONDS)
        httpClient.addInterceptor { chain ->
            try {
                val original = chain.request()
//                val timestamp = REQUEST_TIMESTAMP_VALUE()
                val url = original.url().newBuilder()
//                    .addQueryParameter(VERSION_KEY, VERSION_VALUE)
//                    .addQueryParameter(TIMESTAMP_KEY, timestamp)
//                    .addQueryParameter(UUID_KEY, DeviceInfo.UUID())
                    .build()

                val request = original.newBuilder()
                    .url(url)
                    .header(FORMAT_KEY, FORMAT_VALUE)
                    .method(original.method(), original.body())
                    .build()

                Log.e(TAG, "head:" + "\n" + request.headers().toString())
                Log.e(TAG, "request:$request")

                if (request.method() == "POST") {
                    val  sb = StringBuilder()
                    if (request.body() is FormBody) {
                        val body = request as FormBody?

                        for (i in 0 until (body?.size() ?: 0)) {
                            sb.append(body?.encodedName(i) + "=" + body?.encodedValue(i) + ",")
                        }
                        sb.delete(sb.length - 1, sb.length)
                        Log.d(TAG, "| body:{$sb}")
                        Log.d(TAG, request.body().toString())
                    }
                }

                return@addInterceptor chain.proceed(request)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            null
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)
        val client = httpClient.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <T> create(clazz: Class<T>): T = getRetrofit().create(clazz)
}