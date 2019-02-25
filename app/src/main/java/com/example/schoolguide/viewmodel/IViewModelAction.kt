package com.example.schoolguide.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.example.schoolguide.event.BaseActionEvent

interface IViewModelAction {
    fun startLoading()

    fun startLoading(message: String?)

    fun dismissingLoading()

    fun showToast(message: String?)

    fun finish()

    fun finishWithResultOk()

    var getActionLiveData: MutableLiveData<BaseActionEvent>
}