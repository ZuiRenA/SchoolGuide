package com.example.schoolguide.guide

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.SelectDor
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class SelectDorViewModel : ViewModel() {
    var dormitoryLiveData: MutableLiveData<isSuccess<List<Dormitory>>>? = MutableLiveData()
    var selectDorLiveData: MutableLiveData<isSuccess<String>>? = MutableLiveData()

    fun dormitory(id: Int) {
        RetrofitHelper.create(Server::class.java).dormitory(id).callback({
            dormitoryLiveData?.value = null
        }) {
            dormitoryLiveData?.value = it.body()
        }
    }

    fun select(phone: Long, index1: Int, index2: Int) {
        RetrofitHelper.create(Server::class.java).selectDor(SelectDor(phone, index1, index2)).callback({
            selectDorLiveData?.value = null
        }) {
            selectDorLiveData?.value = it.body()
        }
    }
}