package com.example.schoolguide.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.MainActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.util.LoginUtil.saveLoginInfo
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginRegister -> {
                this.intent(RegisterActivity::class.java)
            }
            R.id.loginForgetPassword -> {
                this.intent(ForgetPswActivity::class.java)
            }
            R.id.loginBtn -> {
                login()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginAccount.inputType = InputType.TYPE_CLASS_NUMBER
        initClick()
    }

    private fun initClick() {
        loginRegister.setOnClickListener(this)
        loginForgetPassword.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }

    private fun login() {
        viewModel.loginLiveData?.observe(this, Observer {
            if (it != null) {
                if (it.isSuccess) {
                    val user = it.respond
                    saveLoginInfo(user.phone_number.toString(), user.password)
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

        if (loginAccount.text.isEmpty() or loginPassword.text.isEmpty()) {
            toast(getString(R.string.login_btn_error))
        } else {
            viewModel.login(loginAccount.text.toString().toLong(), loginPassword.text.toString())
        }
    }
}
