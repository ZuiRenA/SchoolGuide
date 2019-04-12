package com.example.schoolguide.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.example.schoolguide.model.PanP
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server
import com.example.schoolguide.util.LoginUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    var loginLiveData: MutableLiveData<isSuccess<User>> ?= MutableLiveData()
    var registerLiveData: MutableLiveData<isSuccess<User>> ?= MutableLiveData()

    fun login(phoneNumber: Long, password: String) {
        RetrofitHelper.create(Server::class.java).login(PanP(phoneNumber, password)).enqueue(object : Callback<isSuccess<User>> {
            override fun onFailure(call: Call<isSuccess<User>>, t: Throwable) {
                loginLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<User>>, response: Response<isSuccess<User>>) {
                if (response.body()!!.isSuccess) {
                    LoginUtil.user = response.body()!!.respond
                }

                loginLiveData?.value = response.body()
            }
        })
    }

    fun register(name: String, phoneNumber: Long, password: String) {
        RetrofitHelper.create(Server::class.java).register(User(name = name, phone_number = phoneNumber, password = password)).enqueue(object : Callback<isSuccess<User>>  {
            override fun onFailure(call: Call<isSuccess<User>>, t: Throwable) {
                registerLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<User>>, response: Response<isSuccess<User>>) {
                if (response.body()!!.isSuccess) {
                    LoginUtil.user = response.body()!!.respond
                }

                registerLiveData?.value = response.body()
            }
        })
    }
}