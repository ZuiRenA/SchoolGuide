package com.example.schoolguide.mine

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.AppContext
import com.example.schoolguide.extUtil.callback
import com.example.schoolguide.extUtil.no
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.model.Password
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server

class ChangePasswordViewModel : ViewModel() {
    var messageLiveData: MutableLiveData<isSuccess<Int>>? = MutableLiveData()
    var passwordLiveData: MutableLiveData<isSuccess<User>>? = MutableLiveData()


    fun sendMessage(phone: String) {
        RetrofitHelper.create(Server::class.java).sendMessage(phone).callback({
            messageLiveData?.value = null
        }) {
            messageLiveData?.value = it.body()
        }
    }

    fun changePassword(phoneNumber: Long, password: String) {
        RetrofitHelper.create(Server::class.java).changePassword(password = Password(phoneNumber, password)).callback({
            passwordLiveData?.value = null
        }) {
            passwordLiveData?.value = it.body()
            it.body().let { result ->
                result?.let { response ->
                    response.isSuccess.no {
                        response.errorReason?.let { it -> AppContext.toast(it) }
                    }
                }
            }
        }
    }
}