package com.example.schoolguide.extUtil

import android.view.View

fun View.onClick(block: View.() -> Unit) {
    setOnClickListener {
        block()
    }
}