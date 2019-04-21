package com.example.schoolguide.network

import com.example.schoolguide.model.*
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

    @POST("personData")
    fun personUpload(@Body user: User): Call<isSuccess<User>>

    @GET("school")
    fun schoolList(): Call<isSuccess<List<School>>>

    @GET("college/{id}")
    fun collegeList(@Path("id") id: Int): Call<isSuccess<List<String>>>

    @POST("upload/letter")
    fun uploadLetterCA(@Body letter: Letter): Call<isSuccess<User>>

    @GET("guideTime/{id}")
    fun guideTImeList(@Path("id") id: Int): Call<isSuccess<List<SchoolGuideTime>>>
}