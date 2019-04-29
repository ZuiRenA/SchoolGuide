package com.example.schoolguide.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import kotlinx.android.synthetic.main.activity_forget_psw.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgetPswActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel
    private var isShow = false
    private var message: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_psw)
        viewModel = viewModelProvider()

        initClick()
        initNetwork()
    }

    private fun initNetwork() {
        observerAction(viewModel.messageLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    message = it.respond
                }.otherwise { response.errorReason?.let { error ->
                    toast(error)
                } }
            }
        }

        observerAction(viewModel.passwordLiveData) {
            it?.let { response ->
                response.isSuccess.yes {
                    toast(getString(R.string.change_password_success))
                    startActivity<LoginActivity> {  }
                    finish()
                }
            }
        }
    }

    private fun initClick() {
        btnCDForget.onClick {
            if (forgetPhoneNumber.text.isEmpty() || forgetPhoneNumber.text.length != 11) {
                toast(getString(R.string.phone_error))
            } else {
                viewModel.sendMessage(forgetPhoneNumber.text.toString())
                btnCDForget.sendVerifyCode()
            }
        }

        forgetPswShow.onClick {
            if (isShow) {
                isShow = false
                forgetPswShow.setImageResource(R.drawable.password_unshow)
                forgetPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                isShow = true
                forgetPswShow.setImageResource(R.drawable.password_show)
                forgetPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }

        forgetBtnAssign.onClick {
            viewModel.changePassword(forgetPhoneNumber.text.toString().toLong(), forgetPassword.text.toString())
        }
    }
}
