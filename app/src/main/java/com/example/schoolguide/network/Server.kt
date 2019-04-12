package com.example.schoolguide.network

import com.example.schoolguide.model.PanP
import com.example.schoolguide.model.SchoolInfo
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Server {

    @GET("schoolInfo/{id}")
    fun schoolInfo(@Path("id") id: Int) : Call<isSuccess<SchoolInfo>>

    @POST("login")
    fun login(@Body  content: PanP) : Call<isSuccess<User>>

    @POST("register")
    fun register(@Body user: User): Call<isSuccess<User>>
}