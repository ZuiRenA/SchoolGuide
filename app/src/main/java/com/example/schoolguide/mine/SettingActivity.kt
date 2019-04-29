package com.example.schoolguide.mine

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.schoolguide.view.BaseActivity
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.intent
import com.example.schoolguide.extUtil.onClick
import com.example.schoolguide.extUtil.toast
import com.example.schoolguide.login.LoginActivity
import com.example.schoolguide.model.LogoutEvent
import com.example.schoolguide.util.CacheUtil
import com.example.schoolguide.util.LoginUtil
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.base_toolbar.*
import org.greenrobot.eventbus.EventBus

class SettingActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        baseToolbarTitle.text = getString(R.string.setting)
        baseToolbar.setNavigationOnClickListener { finish() }
        cacheSize.text = CacheUtil.getTotalCacheSize(applicationContext)

        initClick()
    }

    private fun initClick() {
        logout.setOnClickListener(this)
        clearCache.setOnClickListener(this)
        changePSW.onClick {

        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.logout -> {  logout()  }
            R.id.clearCache -> {
                val isClearCache = CacheUtil.clearAllCache(applicationContext)
                if (isClearCache)  {
                    toast(getString(R.string.clear_cache_success))
                    cacheSize.text = CacheUtil.getTotalCacheSize(applicationContext)
                } else toast(getString(R.string.clear_cache_fail))
            }
        }
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.login_out_sure)
            .setPositiveButton(R.string.assign) { _, _ ->
                LoginUtil.clearLoginInfo()
                intent(LoginActivity::class.java)
                EventBus.getDefault().post(LogoutEvent())
                finish()
            }
            .setNegativeButton(R.string.cancel) { _, _-> }
        builder.create().show()
    }

}
