package com.example.schoolguide.util

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.AppContext

/**
 * 解决glide无法对placeholder()的占位图进行剪裁的问题
 */
fun loadTransform(@DrawableRes drawable: Int, block: () -> RequestOptions) =
    Glide.with(AppContext).load(drawable).apply(block())

fun loadTransform(drawable: Drawable, block: () -> RequestOptions) =
    Glide.with(AppContext).load(drawable).apply(block())
