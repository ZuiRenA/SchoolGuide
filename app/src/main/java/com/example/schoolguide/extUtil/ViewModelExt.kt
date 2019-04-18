package com.example.schoolguide.extUtil

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

/**
 * viewModel.MutableLiveData网络请求写法的简易封装
 * 使用方法:
 *  LifecycleOwner.observerAction(mutableLiveData) {  action() }
 */
fun <T> LifecycleOwner.observerAction(liveData: MutableLiveData<T>?, block: (T?) -> Unit) {
    liveData?.observe(this, Observer {
        block(it)
    })
}