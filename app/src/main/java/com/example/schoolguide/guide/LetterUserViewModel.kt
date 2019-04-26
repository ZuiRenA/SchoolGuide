package com.example.schoolguide.guide

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.model.Letter
import com.example.schoolguide.model.User
import com.example.schoolguide.model.isSuccess
import com.example.schoolguide.network.RetrofitHelper
import com.example.schoolguide.network.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LetterUserViewModel: ViewModel() {
    val uploadLetterLiveData: MutableLiveData<isSuccess<User>>? = MutableLiveData()

    fun uploadLetter(phone: Long ,uri: String) {
        RetrofitHelper.create(Server::class.java).uploadLetterCA(Letter(phone_number = phone,uri = uri)).enqueue(object : Callback<isSuccess<User>> {
            override fun onFailure(call: Call<isSuccess<User>>, t: Throwable) {
                uploadLetterLiveData?.value = null
            }

            override fun onResponse(call: Call<isSuccess<User>>, response: Response<isSuccess<User>>) {
                uploadLetterLiveData?.value = response.body()
            }
        })
    }
}