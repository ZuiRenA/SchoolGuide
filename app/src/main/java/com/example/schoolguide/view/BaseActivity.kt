package com.example.schoolguide.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.kaopiz.kprogresshud.KProgressHUD

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
    }

    private var hud: KProgressHUD? = null

    fun showLoading() {
         hud = KProgressHUD.create(this)
             .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
             .setDimAmount(0.5f)
             .setCancellable(false)
             .show()
    }

    fun dismissLoading() {
        hud?.dismiss()
    }
}