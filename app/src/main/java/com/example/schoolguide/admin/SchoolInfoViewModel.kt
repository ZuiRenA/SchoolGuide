package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.SchoolInfo
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class SchoolInfoViewModel: ViewModel() {
    var updateSchoolLiveData: MutableLiveData<isSuccess<SchoolInfo>> = MutableLiveData()

    fun updateSchool(schoolInfo: SchoolInfo) {
        RetrofitHelper.create(Server::class.java).updateSchool(schoolInfo).callback({
            Log.e("retorfit error:", "$it")
            updateSchoolLiveData.value = null
        }) {
            updateSchoolLiveData.value = it.body()
        }
    }
}