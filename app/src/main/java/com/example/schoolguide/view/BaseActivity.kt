package com.example.schoolguide.view

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.schoolguide.R
import com.kaopiz.kprogresshud.KProgressHUD

@SuppressLint("Registered")
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