package com.example.schoolguide.extUtil

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.callback(failBlock: (Throwable) -> Unit, successBlock: (Response<T>) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            failBlock(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            successBlock(response)
        }
    })
}