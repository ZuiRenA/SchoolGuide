package com.example.schoolguide.extUtil

import android.arch.lifecycle.*
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

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

inline fun <reified T : ViewModel?> Fragment.viewModelProvider(): T = ViewModelProviders.of(this).get(T::class.java)
inline fun <reified T : ViewModel?> FragmentActivity.viewModelProvider(): T = ViewModelProviders.of(this).get(T::class.java)