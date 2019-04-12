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
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.util.LoginUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), View.OnClickListener {
    private var isShow = false
    private lateinit var viewModel: LoginViewModel

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
                if (registerName.text.isEmpty() or registerPhoneNumber.text.isEmpty() or registerPassword.text.isEmpty()) {
                    toast(getString(R.string.register_btn_error))
                } else {
                    register()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        initClick()
    }

    private fun initClick() {
        registerPswShow.setOnClickListener(this)
        registerBtn.setOnClickListener(this)
    }

    private fun register() {
        viewModel.registerLiveData?.observe(this, Observer {
            if (it != null) {
                if (it.isSuccess) {
                    val user = it.respond
                    LoginUtil.saveLoginInfo(user.phone_number.toString(), user.password)
                    this.intent(MainActivity::class.java)
                    this.finish()
                } else {
                    it.errorReason?.let { error ->
                        toast(error)
                    }
                }
            } else {
                toast(getString(R.string.network_error))
            }
        })

        viewModel.register(
            registerName.text.toString(),
            registerPhoneNumber.text.toString().toLong(),
            registerPassword.text.toString()
        )
    }
}
