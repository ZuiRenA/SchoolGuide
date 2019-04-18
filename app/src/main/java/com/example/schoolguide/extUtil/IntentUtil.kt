package com.example.schoolguide.extUtil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * intent的简易封装
 * 使用方法: this.intent(cls) / context.intent(cls)
 * 如需要传值 this.intent(cls, map(a to b, c to d, e to f))
 * @cls: 跳转页面
 * @content: 跳转页面时需要传递的值
 */
fun <T> Activity.intent(
    cls: Class<T>,
    content: Map<String, Any>? = null
) {
    val intent = Intent(this, cls)
    content?.let {
        content.forEach { (key, value) ->
            switchValue(intent, key, value)
        }
    }
    startActivity(intent)
}

fun <T> Context.intent(
    cls: Class<T>,
    content: Map<String, Any>? = null
) {
    val intent = Intent(this, cls)
    content?.let {
        content.forEach { (key, value) ->
            switchValue(intent, key, value)
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivity(block: () -> Any) {
    val intent = Intent(this, T::class.java)
    (block() !is Unit).yes {
        (block() as Map<String, Any>).forEach { map ->
            switchValue(intent, map.key, map.value)
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Context.startActivity(block: () -> Any) {
    val intent = Intent(this, T::class.java)
    (block() !is Unit).yes {
        (block() as Map<String, Any>).forEach { map ->
            switchValue(intent, map.key, map.value)
        }
    }
    startActivity(intent)
}

fun switchValue(intent: Intent, key: String, value: Any) {
    when (value) {
        is Bundle -> intent.putExtra(key, value)
        is Parcelable -> intent.putExtra(key, value)
        is Serializable -> intent.putExtra(key, value)
        is ArrayList<*> -> intent.putExtra(key, value)
        is Boolean -> intent.putExtra(key, value)
        is Byte -> intent.putExtra(key, value)
        is Char -> intent.putExtra(key, value)
        is CharSequence -> intent.putExtra(key, value)
        is Double -> intent.putExtra(key, value)
        is Float -> intent.putExtra(key, value)
        is Int -> intent.putExtra(key, value)
        is Long -> intent.putExtra(key, value)
        is Short -> intent.putExtra(key, value)
        is String -> intent.putExtra(key, value)
        is Array<*> -> intent.putExtra(key, value)
        is IntArray -> intent.putExtra(key, value)
        is BooleanArray -> intent.putExtra(key, value)
        is ByteArray -> intent.putExtra(key, value)
        is CharArray -> intent.putExtra(key, value)
        is DoubleArray -> intent.putExtra(key, value)
        is FloatArray -> intent.putExtra(key, value)
        is LongArray -> intent.putExtra(key, value)
        is ShortArray -> intent.putExtra(key, value)
        else -> throw RuntimeException("Intent Have Error value type")
    }
}






