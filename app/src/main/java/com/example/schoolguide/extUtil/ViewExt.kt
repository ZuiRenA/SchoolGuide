package com.example.schoolguide.extUtil

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import android.widget.AdapterView
import android.widget.PopupWindow
import com.example.schoolguide.util.PopupWindowList


@SuppressLint("StaticFieldLeak")
private var popWindow: PopupWindowList? = null

fun View.showMorePopWindowMenu(dataList: List<String>, block:(parent: AdapterView<*>, view: View, position: Int, id: Long) -> Unit) {
    if (popWindow == null) {
        popWindow = PopupWindowList(this.context)
    }

    popWindow?.apply {
        setAnchorView(this@showMorePopWindowMenu)
        setItemData(dataList)
        setModal(true)
        show()
        setOnItemClickListener { parent, view, position, id ->
            block(parent, view, position, id)
        }
    }
}