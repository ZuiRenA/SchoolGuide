package com.example.schoolguide

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
    }
}