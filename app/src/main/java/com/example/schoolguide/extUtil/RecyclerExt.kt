package com.example.schoolguide.extUtil

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

fun RecyclerView.setLayoutManager(action:() -> RecyclerView.LayoutManager) {
    this.layoutManager = action()
}

fun <T: View> BaseViewHolder.action(@IdRes viewId: Int, action:(T) -> Unit): BaseViewHolder {
    val view = getView<T>(viewId)
    action(view)
    return this
}