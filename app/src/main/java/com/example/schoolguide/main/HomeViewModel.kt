package com.example.schoolguide.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.model.SchoolGuideTime
import com.example.schoolguide.model.SchoolInfo
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    val schoolInfoLiveData: MutableLiveData<isSuccess<SchoolInfo>>? = MutableLiveData()
    val guideTimeLiveData: MutableLiveData<isSuccess<List<SchoolGuideTime>>>? = MutableLiveData()

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

    fun guideTime(id: Int) {
        RetrofitHelper.create(Server::class.java).guideTImeList(id)
            .enqueue(object : Callback<isSuccess<List<SchoolGuideTime>>> {
                override fun onFailure(call: Call<isSuccess<List<SchoolGuideTime>>>, t: Throwable) {
                    guideTimeLiveData?.value = null
                }

                override fun onResponse(
                    call: Call<isSuccess<List<SchoolGuideTime>>>,
                    response: Response<isSuccess<List<SchoolGuideTime>>>
                ) {
                    guideTimeLiveData?.value = response.body()
                }
            })
    }
}