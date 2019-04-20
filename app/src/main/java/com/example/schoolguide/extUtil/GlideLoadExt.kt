package com.example.schoolguide.extUtil

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.schoolguide.AppContext
import com.example.schoolguide.util.loadTransform

/**
 * 需求占位图和error图按照要求进行变圆、高斯模糊等需求的Glide扩展
 * @uri : 加载的uri
 * @placeholder: 占位图的资源
 * @error: 加载失败图的资源
 * @block(): 用lambda表达式, 用来完成需要做的行为的动作，例如高斯模糊等等
 */

fun ImageView.show(uri: String?, placeholder: Drawable, error: Drawable, block: () -> RequestOptions) {
    Glide.with(AppContext).load(uri)
        .apply(
            RequestOptions().placeholder(placeholder)
                .error(error)
        )
        .thumbnail(loadTransform(placeholder) {
            block()
        })
        .thumbnail(loadTransform(error) {
            block()
        })
        .apply(block())
        .into(this)
}

fun ImageView.show(uri: String?, placeholder: Int, error: Int, block: () -> RequestOptions) {
    Glide.with(AppContext).load(uri)
        .apply(
            RequestOptions().placeholder(placeholder)
                .error(error)
        )
        .thumbnail(loadTransform(placeholder) {
            block()
        })
        .thumbnail(loadTransform(error) {
            block()
        })
        .apply(block())
        .into(this)
}

fun ImageView.show(uri: String?, placeholder: Int, error: Drawable, block: () -> RequestOptions) {
    Glide.with(AppContext).load(uri)
        .apply(
            RequestOptions().placeholder(placeholder)
                .error(error)
        )
        .thumbnail(loadTransform(placeholder) {
            block()
        })
        .thumbnail(loadTransform(error) {
            block()
        })
        .apply(block())
        .into(this)
}

fun ImageView.show(uri: String?, placeholder: Drawable, error: Int, block: () -> RequestOptions) {
    Glide.with(AppContext).load(uri)
        .apply(
            RequestOptions().placeholder(placeholder)
                .error(error)
        )
        .thumbnail(loadTransform(placeholder) {
            block()
        })
        .thumbnail(loadTransform(error) {
            block()
        })
        .apply(block())
        .into(this)
}
