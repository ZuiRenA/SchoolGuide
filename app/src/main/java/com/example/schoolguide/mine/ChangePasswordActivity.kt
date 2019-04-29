package com.example.schoolguide.mine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.onClick
import com.example.schoolguide.view.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.base_toolbar.*

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        baseToolbarTitle.text = getString(R.string.change_password_tittle)
        baseToolbar.setNavigationOnClickListener { finish() }
        initClick()
    }

    private fun initClick() {
        btnCPGetMessage.onClick {
            btnCPGetMessage.sendVerifyCode()
        }
    }
}
