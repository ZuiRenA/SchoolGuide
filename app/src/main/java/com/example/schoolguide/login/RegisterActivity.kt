package com.example.schoolguide.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), View.OnClickListener {
    private var isShow = false

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.registerPswShow -> {
                if (isShow) {
                    isShow = false
                    registerPswShow.setImageResource(R.drawable.password_unshow)
                    registerPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    isShow = true
                    registerPswShow.setImageResource(R.drawable.password_show)
                    registerPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
            }

            R.id.registerBtn -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initClick()
    }

    private fun initClick() {
        registerPswShow.setOnClickListener(this)
        registerBtn.setOnClickListener(this)
    }
}
