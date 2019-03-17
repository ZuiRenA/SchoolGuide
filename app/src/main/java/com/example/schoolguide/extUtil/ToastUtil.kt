package com.example.schoolguide.extUtil

import android.app.Activity
import android.content.Context
import android.widget.Toast

/**
 * Toast的简易封装
 * 使用方法: this.toast(message) / context.toast(message)
 */
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}