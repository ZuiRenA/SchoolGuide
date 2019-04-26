package com.example.schoolguide.login

import android.os.Bundle
import android.text.InputType
import android.view.View
import com.example.schoolguide.MainActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.util.LoginUtil.saveLoginInfo
import com.example.schoolguide.view.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

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
        viewModel = this.viewModelProvider()
        loginAccount.inputType = InputType.TYPE_CLASS_NUMBER
        initClick()
        initNetwork()
    }

    private fun initNetwork() {
        observerAction(viewModel.loginLiveData) {
            it?.let {response ->
                response.isSuccess.yes {
                    val user = it.respond
                    saveLoginInfo(user.phone_number.toString(), user.password)
                    startActivity<MainActivity> {  }
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
        loginRegister.setOnClickListener(this)
        loginForgetPassword.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }

    private fun login() {
        if (loginAccount.text.isEmpty() or loginPassword.text.isEmpty()) {
            toast(getString(R.string.login_btn_error))
        } else {
            viewModel.login(loginAccount.text.toString().toLong(), loginPassword.text.toString())
        }
    }
}
