package com.example.schoolguide.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.model.SchoolInfo
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    val schoolInfoLiveData: MutableLiveData<isSuccess<SchoolInfo>>? = MutableLiveData()
    val baseLiveData: MutableLiveData<String> ?= MutableLiveData()

    fun schoolInfo(id: Int) {
        RetrofitHelper.create(Server::class.java).schoolInfo(id).enqueue(object : Callback<isSuccess<SchoolInfo>> {
            override fun onFailure(call: Call<isSuccess<SchoolInfo>>, t: Throwable) {
                schoolInfoLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<SchoolInfo>>, response: Response<isSuccess<SchoolInfo>>) {
                schoolInfoLiveData?.value = response.body()
            }
        })
    }
}