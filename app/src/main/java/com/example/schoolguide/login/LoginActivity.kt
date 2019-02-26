package com.example.schoolguide.login

import android.os.Bundle
import android.view.View
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.MainActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.util.LoginUtil.saveLoginInfo
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.loginRegister ->{
                this.intent(RegisterActivity::class.java)
            }
            R.id.loginForgetPassword -> {
                this.intent(ForgetPswActivity::class.java)
            }
            R.id.loginBtn -> {
                saveLoginInfo(loginAccount.text.toString(), loginPassword.text.toString())
                this.intent(MainActivity::class.java)
                this.finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initClick()
    }

    private fun initClick() {
        loginRegister.setOnClickListener(this)
        loginForgetPassword.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }
}
