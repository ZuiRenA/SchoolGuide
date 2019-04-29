package com.example.schoolguide.mine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.login.LoginActivity
import com.example.schoolguide.model.LogoutEvent
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.base_toolbar.*
import org.greenrobot.eventbus.EventBus

class ChangePasswordActivity : BaseActivity() {
    private lateinit var viewModel: ChangePasswordViewModel
    private var message: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        viewModel = viewModelProvider()

        baseToolbarTitle.text = getString(R.string.change_password_tittle)
        baseToolbar.setNavigationOnClickListener { finish() }
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
                    LoginUtil.clearLoginInfo()
                    EventBus.getDefault().post(LogoutEvent())
                    startActivity<LoginActivity> {  }
                    finish()
                }
            }
        }
    }

    private fun initClick() {
        btnCPGetMessage.onClick {
            if (editCPPhoneNumber.text.length != 11)
                toast(getString(R.string.phone_error))
            else {
                btnCPGetMessage.sendVerifyCode()
                viewModel.sendMessage(editCPPhoneNumber.text.toString())
            }
        }

        btnCPAssign.onClick {
            if (editCPMessage.text.toString().toInt() == message) {
                if (editCPPassword.text.isEmpty()) {
                    toast(getString(R.string.password_null_error))
                } else {
                    LoginUtil.user?.phone_number?.let {
                        viewModel.changePassword(phoneNumber = it, password = editCPPassword.text.toString())
                    }
                }
            } else {
                toast(getString(R.string.message_error))
            }
        }
    }
}
