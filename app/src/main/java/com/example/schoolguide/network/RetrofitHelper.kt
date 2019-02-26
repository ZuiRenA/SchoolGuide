package com.example.schoolguide.network

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import com.example.schoolguide.App
import com.example.schoolguide.network.NetConfig.BASE_URL
import com.example.schoolguide.network.NetConfig.TIMESTAMP_KEY
import com.example.schoolguide.network.NetConfig.UUID_KEY
import com.example.schoolguide.network.NetConfig.VERSION_KEY
import com.example.schoolguide.network.NetConfig.VERSION_VALUE
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp

object RetrofitHelper {

    fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            try {
                val original = chain.request()
                val timestamp = REQUEST_TIMESTAMP_VALUE()
                val url = original.url().newBuilder()
                    .addQueryParameter(VERSION_KEY, VERSION_VALUE)
                    .addQueryParameter(TIMESTAMP_KEY, timestamp)
                    .addQueryParameter(UUID_KEY, DeviceInfo.UUID())
                    .build()

                val packageInfo = App.instance.packageManager.getPackageInfo(App.instance.packageName, 0)
                Log.v("mac: ", DeviceInfo.unique_sign(App.instance))
                val request = original.newBuilder()
                    .url(url)
                    .method(original.method(), original.body())
                    .build()

                Log.v(TAG, "head:" + "\n" + request.headers().toString())
                Log.v(TAG, "request:$request")

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

    fun REQUEST_TIMESTAMP_VALUE(): String =
            "" + Timestamp(System.currentTimeMillis()).time / 1000
}