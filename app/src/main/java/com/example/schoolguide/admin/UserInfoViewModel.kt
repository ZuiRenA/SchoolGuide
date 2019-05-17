package com.example.schoolguide.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.model.Dormitory
import com.example.schoolguide.model.Id
import com.example.schoolguide.model.School
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class UserInfoViewModel: ViewModel() {
    var schoolListLiveData: MutableLiveData<isSuccess<List<School>>> = MutableLiveData()
    var collegeListLiveData: MutableLiveData<isSuccess<List<String>>> = MutableLiveData()
    var dormitoryListLiveData: MutableLiveData<isSuccess<List<Dormitory>>> = MutableLiveData()

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
}