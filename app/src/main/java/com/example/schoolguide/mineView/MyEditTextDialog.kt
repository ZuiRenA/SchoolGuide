package com.example.schoolguide.mineView

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.example.schoolguide.App
import com.example.schoolguide.AppContext
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.yes
import kotlinx.android.synthetic.main.edit_text_dialog.*

class MyEditTextDialog(context: Context, style: Int = R.style.DialogCommon) : Dialog(context, style) {
    private var title: String? = null
    private var message: String? = null
    private var optionOneStr: String? = null
    private var optionTwoStr: String? = null
    private var onOptionOneListener: OnOptionOneClickListener? = null
    private var onOptionTwoListener: OnOptionTwoClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_text_dialog)
        setCanceledOnTouchOutside(false)

        initData()
        initEvent()
    }

    private fun initEvent() {
        dialogOptionOne.setOnClickListener {
            onOptionOneListener?.let { it.onOptionOneClick(dialogEditText.text.toString()) }
        }

        dialogOptionTwo.setOnClickListener {
            onOptionTwoListener?.let { it.onOptionTwoClick() }
        }
    }

        private fun initData() {
        title?.let { dialogTitle.text = it }
        message?.let { dialogMessage.text = it }
        optionOneStr?.let { dialogOptionOne.text = it }
        optionTwoStr?.let { dialogOptionTwo.text = it }
    }

    fun setTitle(str: String) {
        title = str
    }

    fun setMessage(str: String) {
        message = str
    }

    internal fun setOptionOneOnclickListener(str: String?, onOptionListener: OnOptionOneClickListener) {
        str?.let{ optionOneStr = it }
        this.onOptionOneListener = onOptionListener
    }

    internal fun setOptionTwoOnclickListener(str: String?, onOptionListener: OnOptionTwoClickListener) {
        str?.let { optionTwoStr = it }
        this.onOptionTwoListener = onOptionListener
    }

    interface OnOptionOneClickListener {
        fun onOptionOneClick(content: String?)

    }
    interface OnOptionTwoClickListener {
        fun onOptionTwoClick()
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

