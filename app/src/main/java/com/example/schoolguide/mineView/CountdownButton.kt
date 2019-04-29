package com.example.schoolguide.mineView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.Button
import com.example.schoolguide.R

class CountdownButton(context: Context, attributeSet: AttributeSet) : Button(context, attributeSet) {
    private val mHandler: Handler = Handler()
    private var countTime = 60

    init {
        this.text = "获取验证码"
    }

    fun sendVerifyCode() {
        mHandler.postDelayed(countDown, 0)
    }

    private val countDown = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            this@CountdownButton.text = countTime.toString() + "s "
            this@CountdownButton.background = resources.getDrawable(R.drawable.change_password_message)
            this@CountdownButton.setTextColor(resources.getColor(R.color.black))
            this@CountdownButton.isEnabled = false
            if (countTime > 0) {
                mHandler.postDelayed(this, 1000)
            } else {
                resetCounter()
            }
            countTime--
        }
    }

    fun removeRunable() {
        mHandler.removeCallbacks(countDown)
    } //重置按钮状态

    fun resetCounter(vararg text: String) {
        this.isEnabled = true
        if (text.isNotEmpty() && "" != text[0]) {
            this.text = text[0]
        } else {
            this.text = "获取验证码"
        }

        this.background = resources.getDrawable(R.drawable.change_password_message)
        this.setTextColor(resources.getColor(R.color.black))
        countTime = 60
    }
}