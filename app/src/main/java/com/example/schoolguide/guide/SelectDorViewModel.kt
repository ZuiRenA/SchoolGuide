package com.example.schoolguide.guide

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class SelectDorViewModel : ViewModel() {
    var dormitoryLiveData: MutableLiveData<isSuccess<List<Dormitory>>>? = MutableLiveData()

    fun dormitory(id: Int) {
        RetrofitHelper.create(Server::class.java).dormitory(id).callback({
            dormitoryLiveData?.value = null
        }) {
            dormitoryLiveData?.value = it.body()
        }
    }
}