package com.example.schoolguide

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.login.LoginActivity
import com.example.schoolguide.util.LoginUtil
import com.example.schoolguide.view.BaseActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImg.visibility = View.VISIBLE
        Glide.with(this).load(R.mipmap.gif_02)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(splashImg)

        val intentAction = GlobalScope.launch {
            delay(3000L)
            getLoginInfoTodo()
        }

        splashSkip.setOnClickListener {
            intentAction.cancel()
            getLoginInfoTodo()
        }


    }

    private fun getLoginInfoTodo() {
        val loginInfo = LoginUtil.getLoginInfo()
        if (loginInfo.account != null && loginInfo.password != null) {
            intent(MainActivity::class.java)
            finish()
        } else {
            intent(LoginActivity::class.java)
            finish()
        }
    }
}
