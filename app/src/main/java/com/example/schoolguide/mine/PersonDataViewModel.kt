package com.example.schoolguide.mine

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.model.School
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server
import com.example.schoolguide.util.LoginUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonDataViewModel : ViewModel() {
    val uploadLiveData: MutableLiveData<isSuccess<User>>? = MutableLiveData()
    val schoolLiveData: MutableLiveData<isSuccess<List<School>>>? = MutableLiveData()
    val collegeLiveData: MutableLiveData<isSuccess<List<String>>> ?= MutableLiveData()

    fun upload(name: String? = null, avatar: String? = null, school: String? = null,
               college: String? = null, userName: String? = null, idCard: String? = null) {
        val info = LoginUtil.user!!
        val user = User(id = info.id, name = name ?: info.name, phone_number = info.phone_number, password = info.password, user_avatar = avatar, user_school = school,
            user_college = college, user_name = userName, user_id_card = idCard, user_dormitory = null)
        RetrofitHelper.create(Server::class.java).personUpload(user).enqueue(object : Callback<isSuccess<User>> {
            override fun onFailure(call: Call<isSuccess<User>>, t: Throwable) {
                uploadLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<User>>, response: Response<isSuccess<User>>) {
                response.body()?.respond.let {
                    LoginUtil.user = it
                }

                uploadLiveData?.value = response.body()
            }
        })
    }

    fun school() {
        RetrofitHelper.create(Server::class.java).schoolList().enqueue(object : Callback<isSuccess<List<School>>> {
            override fun onFailure(call: Call<isSuccess<List<School>>>, t: Throwable) {
                schoolLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<List<School>>>, response: Response<isSuccess<List<School>>>) {
                schoolLiveData?.value = response.body()
            }
        })
    }

    fun college(schoolId: Int) {
        RetrofitHelper.create(Server::class.java).collegeList(id = schoolId).enqueue(object : Callback<isSuccess<List<String>>> {
            override fun onFailure(call: Call<isSuccess<List<String>>>, t: Throwable) {
                collegeLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<List<String>>>, response: Response<isSuccess<List<String>>>) {
                collegeLiveData?.value = response.body()
            }
        })
    }
}