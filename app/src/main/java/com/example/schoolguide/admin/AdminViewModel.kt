package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.*
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class AdminViewModel : ViewModel() {
    var userTableLiveData: MutableLiveData<isSuccess<List<User>>> = MutableLiveData()
    var schoolTableInfoLiveData: MutableLiveData<isSuccess<SchoolInfo>> = MutableLiveData()
    var guideTimeLiveData: MutableLiveData<isSuccess<List<SchoolGuideTime>>> = MutableLiveData()
    var dormitoryTableLiveData: MutableLiveData<isSuccess<List<Dormitory>>> = MutableLiveData()

    var deleteUserLiveData: MutableLiveData<isSuccess<Void>> = MutableLiveData()
    var deleteCollegeLiveData: MutableLiveData<isSuccess<Void>> = MutableLiveData()
    var deleteDormitoryLiveData: MutableLiveData<isSuccess<Void>> = MutableLiveData()

    fun getUserTable() {
        RetrofitHelper.create(Server::class.java).userTable().callback({
            Log.e("retrofit error:", "$it")
            userTableLiveData.value = null
        }) {
            userTableLiveData.value = it.body()
        }
    }

    fun getSchoolInfo(id: Int) {
        RetrofitHelper.create(Server::class.java).schoolInfo(id).callback({
            Log.e("retrofit error:", "$it")
            schoolTableInfoLiveData.value = null
        }) {
            schoolTableInfoLiveData.value = it.body()
        }
    }

    fun getGuideTime(id: Int) {
        RetrofitHelper.create(Server::class.java).guideTimeList(id).callback({
            Log.e("retrofit error:", "$it")
            guideTimeLiveData.value = null
        }) {
            guideTimeLiveData.value = it.body()
        }
    }

    fun getDormitory(id: Int) {
        RetrofitHelper.create(Server::class.java).dormitory(id).callback({
            Log.e("retrofit error:", "$it")
            dormitoryTableLiveData.value = null
        }) {
            dormitoryTableLiveData.value = it.body()
        }
    }

    fun deleteUser(phone: Long) {
        RetrofitHelper.create(Server::class.java).deleteUser(phone = phone).callback({
            Log.e("retrofit error:", "$it")
            deleteUserLiveData.value = null
        }) {
            deleteUserLiveData.value = it.body()
        }
    }

    fun deleteCollegeTime(name: String) {
        RetrofitHelper.create(Server::class.java).deleteGuideTime(name).callback({
            Log.e("retrofit error:", "$it")
            deleteCollegeLiveData.value = null
        }) {
            deleteCollegeLiveData.value = it.body()
        }
    }

    fun deleteDormitory(id: Int) {
        RetrofitHelper.create(Server::class.java).deleteDormitory(id).callback({
            Log.e("retrofit error:", "$it")
            deleteDormitoryLiveData.value = null
        }) {
            deleteDormitoryLiveData.value = it.body()
        }
    }
}