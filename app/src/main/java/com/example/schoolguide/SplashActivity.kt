package com.example.schoolguide

import android.Manifest
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class SplashActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {
    private var intentAction: Job = GlobalScope.launch {
        delay(3000L)
        getLoginInfoTodo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImg.visibility = View.VISIBLE
        Glide.with(this).load(R.mipmap.gif_02)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(splashImg)

        splashSkip.setOnClickListener {
            intentAction.cancel()
            getLoginInfoTodo()
        }

        requestPermission()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        private const val PERMISSION = -10001
        private const val PERMISSION_FAIL = "permission_fail"
    }

    @AfterPermissionGranted(PERMISSION)
    private fun requestPermission() {
        val perms = arrayOf(Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (EasyPermissions.hasPermissions(this, *perms)) {
            intentAction.start()
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, PERMISSION, *perms)
                    .setRationale(getString(R.string.permission_request))
                    .setPositiveButtonText(getString(R.string.assign))
                    .setNegativeButtonText(getString(R.string.cancel))
                    .build()
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val mmKvFailCount = MMKV.defaultMMKV()
        var count = mmKvFailCount.decodeInt(PERMISSION_FAIL, 0)
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && count == 0) {
            AppSettingsDialog.Builder(this).build().show()
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && count >= 2) {
            intent(EasterEggActivity::class.java)
        }

        count ++
        mmKvFailCount.encode(PERMISSION_FAIL, count)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }
}
