package com.example.schoolguide.viewmodel.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.schoolguide.event.BaseActionEvent
import com.example.schoolguide.viewmodel.IViewModelAction

class BaseViewModel(): ViewModel(), IViewModelAction {

    private var actionLiveData: MutableLiveData<BaseActionEvent> = MutableLiveData()

    private lateinit var lifecycleOwner: LifecycleOwner

    override fun startLoading() {
        startLoading(null)
    }

    override fun startLoading(message: String?) {
        val baseActionEvent = BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG)
        baseActionEvent.message = message
        actionLiveData.value = baseActionEvent
    }

    override fun dismissingLoading() {
        actionLiveData.value = BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG)
    }

    override fun showToast(message: String?) {
        val baseActionEvent = BaseActionEvent(BaseActionEvent.SHOW_TOAST)
        baseActionEvent.message = message
        actionLiveData.value = baseActionEvent
    }

    override fun finish() {
        actionLiveData.value = BaseActionEvent(BaseActionEvent.FINISH)
    }

    override fun finishWithResultOk() {
        actionLiveData.value = BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK)
    }

    override var getActionLiveData: MutableLiveData<BaseActionEvent> = actionLiveData

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }
}