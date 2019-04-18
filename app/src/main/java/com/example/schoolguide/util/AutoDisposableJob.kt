package com.example.schoolguide.util

import android.view.View
import com.example.schoolguide.extUtil.otherwise
import com.example.schoolguide.extUtil.yes
import kotlinx.coroutines.Job

/**
 *  协程的取消
 */
class AutoDisposableJob(private  val view: View, private val wrapped: Job): Job by wrapped, View.OnAttachStateChangeListener {
    override fun onViewDetachedFromWindow(v: View?) = Unit

    override fun onViewAttachedToWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    init {
         (view.isAttachedToWindow)
             .yes { view.addOnAttachStateChangeListener(this) }
             .otherwise { cancel() }
        invokeOnCompletion {
            view.removeOnAttachStateChangeListener(this)
        }
    }
}

fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)