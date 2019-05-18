package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class DormitoryViewModel : ViewModel() {
    var userTableLiveData: MutableLiveData<isSuccess<List<User>>> = MutableLiveData()
    var insertDorLiveData: MutableLiveData<isSuccess<Void>> = MutableLiveData()
    var updateDorLiveData: MutableLiveData<isSuccess<Void>> = MutableLiveData()

    fun getUserTable(type: Int) {
        RetrofitHelper.create(Server::class.java).userTable().callback({
            Log.e("retrofit error:", "$it")
            userTableLiveData.value = null
        }) {
            it.body()?.errorReason = "$type"
            userTableLiveData.value = it.body()
        }
    }

    fun insertDor(body: Dormitory) {
        RetrofitHelper.create(Server::class.java).insertDor(body).callback({
            Log.e("retrofit error:", "$it")
            insertDorLiveData.value = null
        }) {
            insertDorLiveData.value = it.body()
        }
    }

    fun updateDor(body: Dormitory) {
        RetrofitHelper.create(Server::class.java).updateDor(body).callback({
            Log.e("retrofit error:", "$it")
            updateDorLiveData.value = null
        }) {
            updateDorLiveData.value = it.body()
        }
    }
}