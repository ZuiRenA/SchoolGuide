package com.example.schoolguide.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.schoolguide.BaseActivity
import com.example.schoolguide.MainActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.intent
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
