package com.example.schoolguide.view

import android.support.v4.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD

open class BaseFragment: Fragment () {

    private var hud: KProgressHUD? = null

    fun showLoading() {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setDimAmount(0.5f)
            .setCancellable(false)
            .show()
    }

    fun dismissLoading() {
        hud?.dismiss()
    }
}