package com.example.schoolguide.mineView

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.example.schoolguide.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BaseDialog(context: Context, @LayoutRes val idRes: Int, @StyleRes styleRes: Int = R.style.DialogCommon) :
    Dialog(context, styleRes), View.OnClickListener {

    private var init: DialogInit? = null
    private val views: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }
    private var listener: OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(idRes)
        setCanceledOnTouchOutside(false)

        init?.let {
            it.init()
        }

        views.forEach {
            getView<View>(it).setOnClickListener(this)
        }
    }

    fun <T : View> getView(@IdRes id: Int): T {
        return findViewById(id)
    }

    fun addItemClick(@IdRes id: Int, block: (BaseDialog, View) -> Unit): BaseDialog {
        views.add(id)
        initClick(object : OnItemClickListener{
            override fun onItemClick(dialog: BaseDialog, view: View?) {
                when(view?.id) {
                    id -> {
                        block(this@BaseDialog, view)
                    }
                }
            }
        })

        return this
    }

    fun init(block: (BaseDialog) -> Unit): BaseDialog {
        initDialog(object : DialogInit {
            override fun init() {
                block(this@BaseDialog)
            }
        })
        return this
    }

    private fun initDialog(initDialog: DialogInit) {
        init = initDialog
    }

    private fun initClick(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface DialogInit {
        fun init()
    }

    interface OnItemClickListener {
        fun onItemClick(dialog: BaseDialog, view: View?)
    }

    override fun onClick(v: View?) {
        listener?.let {
            it.onItemClick(this,v)
        }
    }

    override fun show() {
        super.show()
        val layoutParams = window?.attributes
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.decorView?.setPadding(0, 0, 0, 0)
        window?.attributes = layoutParams
    }
}