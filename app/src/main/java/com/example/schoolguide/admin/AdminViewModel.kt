package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class AdminViewModel : ViewModel() {
    var userTableLiveData: MutableLiveData<isSuccess<List<User>>> = MutableLiveData()

    fun getUserTable() {
        RetrofitHelper.create(Server::class.java).userTable().callback({
            Log.d("retrofit error:", "$it")
            userTableLiveData.value = null
        }) {
            userTableLiveData.value = it.body()
        }
    }
}