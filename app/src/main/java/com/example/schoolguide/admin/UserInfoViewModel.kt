package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.*
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class UserInfoViewModel: ViewModel() {
    var schoolListLiveData: MutableLiveData<isSuccess<List<School>>> = MutableLiveData()
    var collegeListLiveData: MutableLiveData<isSuccess<List<String>>> = MutableLiveData()
    var dormitoryListLiveData: MutableLiveData<isSuccess<List<Dormitory>>> = MutableLiveData()
    var addUserLiveData: MutableLiveData<isSuccess<User>> = MutableLiveData()
    var updataLiveData: MutableLiveData<isSuccess<User>> = MutableLiveData()

    fun getSchoolList() {
        RetrofitHelper.create(Server::class.java).schoolList().callback({
            Log.e("retrofit error:", "$it")
            schoolListLiveData.value = null
        }
        ){
            schoolListLiveData.value = it.body()
        }
    }

    fun getCollegeList(id: Int) {
        RetrofitHelper.create(Server::class.java).collegeList(id).callback({
            Log.e("retrofit error:", "$it")
            collegeListLiveData.value = null
        }) {
            collegeListLiveData.value = it.body()
        }
    }

    fun getDorList(id: Int) {
        RetrofitHelper.create(Server::class.java).dormitory(id).callback({
            Log.e("retrofit error:", "$it")
            dormitoryListLiveData.value = null
        }) {
            dormitoryListLiveData.value = it.body()
        }
    }

    fun addUser(user: User) {
        RetrofitHelper.create(Server::class.java).register(user).callback({
            Log.e("retrofit error:", "$it")
            addUserLiveData.value = null
        }) {
            addUserLiveData.value = it.body()
        }
    }

    fun updataUser(user: User) {
        RetrofitHelper.create(Server::class.java).updateUser(user).callback({
            Log.e("retrofit error:", "$it")
            updataLiveData.value = null
        }) {
            updataLiveData.value = it.body()
        }
    }
}