package com.example.schoolguide.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.widget.Toast
import com.example.schoolguide.event.BaseActionEvent
import com.example.schoolguide.viewmodel.IViewModelAction
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseViewModelActivity : BaseActivity() {
    private var loadingDialog: KProgressHUD? = null

    protected abstract fun initViewModel(): ViewModel?

    private fun initViewModelList(): MutableList<ViewModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModelEvent()
    }

    private fun initViewModelEvent() {
        val viewModelList = initViewModelList()

        if (viewModelList != null && viewModelList.size > 0) {
            observeEvent(viewModelList)
        } else {
            val viewModel = initViewModel()
            viewModel?.let {
                val modelList = arrayListOf<ViewModel>()
                modelList.add(it)
                observeEvent(modelList)
            }
        }
    }

    private fun observeEvent(viewModelList: MutableList<ViewModel>) {
        viewModelList.forEach {
            if (it is IViewModelAction) {
                val viewModelAction = it as IViewModelAction
                viewModelAction.getActionLiveData.observe(this, Observer { baseActionEvent ->
                    baseActionEvent?.let {
                        when (baseActionEvent.action) {
                            BaseActionEvent.SHOW_LOADING_DIALOG -> {
                                startLoading(it.message)
                            }

                            BaseActionEvent.DISMISS_LOADING_DIALOG -> {
                                dismissingLoading()
                            }

                            BaseActionEvent.SHOW_TOAST -> {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }

                            BaseActionEvent.FINISH -> {
                                finish()
                            }

                            BaseActionEvent.FINISH_WITH_RESULT_OK -> {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }

                            else -> {
                            }
                        }
                    }
                })
            }
        }
    }

    private fun dismissingLoading() {
        loadingDialog?.let {
            it.dismiss()
        }
    }

    private fun startLoading(message: String?) {
        if (loadingDialog == null) {
            loadingDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setDimAmount(0.5f)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissingLoading()
    }
}