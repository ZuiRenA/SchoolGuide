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

    fun addItemClick(@IdRes id: Int): BaseDialog {
        views.add(id)
        return this
    }

    fun addItemClickList(@IdRes idList: List<Int>, block: (dialog: BaseDialog, view: View?) -> Unit): BaseDialog {
        views.addAll(idList)
        initClick(object : OnItemClickListener {
            override fun onItemClick(dialog: BaseDialog, view: View?) {
                block(dialog, view)
            }
        })
        return this
    }

    fun initClick(listener: OnItemClickListener): BaseDialog {
        this.listener = listener
        return this
    }

    fun init(block: (dialog: BaseDialog) -> Unit): BaseDialog {
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