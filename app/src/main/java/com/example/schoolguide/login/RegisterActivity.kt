package com.example.schoolguide.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.example.schoolguide.MainActivity
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.util.LoginUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), View.OnClickListener {
    private var isShow = false
    private lateinit var viewModel: LoginViewModel
    private var message: Int = 0

    override fun onClick(view: View?) {
        when (view?.id) {
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
                if (registerName.text.isEmpty() or registerPhoneNumber.text.isEmpty() or registerPassword.text.isEmpty() or registerMessage.text.isEmpty()) {
                    toast(getString(R.string.register_btn_error))
                } else {
                    register()
                }
            }

            R.id.btnCDRegister -> {
                if (registerPhoneNumber.text.isEmpty() || registerPhoneNumber.text.length != 11) {
                    toast(getString(R.string.phone_error))
                } else {
                    viewModel.sendMessage(registerPhoneNumber.text.toString())
                    btnCDRegister.sendVerifyCode()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = viewModelProvider()

        initClick()
        initNetWork()
    }

    private fun initNetWork() {
        observerAction(viewModel.messageLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    message = it.respond
                }.otherwise { response.errorReason?.let { error ->
                    toast(error)
                } }
            }
        }

        observerAction(viewModel.registerLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    val user = it.respond
                    LoginUtil.saveLoginInfo(user.phone_number.toString(), user.password)
                    this.intent(MainActivity::class.java)
                    this.finish()
                }.otherwise {
                    response.errorReason?.let { error ->
                        toast(error)
                    }
                }
            }
        }
    }

    private fun initClick() {
        registerPswShow.setOnClickListener(this)
        registerBtn.setOnClickListener(this)
        btnCDRegister.setOnClickListener(this)
    }

    private fun register() {
        if (message == registerMessage.text.toString().toInt()) {
            if (registerPassword.text.isEmpty()) {
                toast(getString(R.string.password_null_error))
            } else {
                viewModel.register(
                    registerName.text.toString(),
                    registerPhoneNumber.text.toString().toLong(),
                    registerPassword.text.toString()
                )
            }

        } else {
            toast(getString(R.string.message_error))
        }
    }
}
