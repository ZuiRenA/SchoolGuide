package com.example.schoolguide.extUtil

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

fun FragmentManager.replace(@IdRes res: Int,fragment: Fragment) {
    this.beginTransaction()
        .replace(res, fragment)
        .commit()
}

fun FragmentManager.action(action:(FragmentTransaction) -> Unit) {
    val fragmentTransaction = this.beginTransaction()
    action(fragmentTransaction)
    fragmentTransaction.commit()
}
