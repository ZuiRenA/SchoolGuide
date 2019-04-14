package com.example.schoolguide.extUtil

import android.content.Context
import com.example.schoolguide.AppContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(val context: Context, val name: String, val default: T, val prefName: String = "default")
    : ReadWriteProperty<Any?, T> {
    private val prefs by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun findPreference(key: String): T =
        with(prefs) {
            when(default) {
                is Long ->getLong(key, default)
                is Int -> getInt(key, default)
                is Boolean -> getBoolean(key, default)
                is Float -> getFloat(key, default)
                is String -> getString(key, default)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        } as T

    private fun putPreference(key: String, value: T) {
        with(prefs.edit()) {
            when(value) {
                is Long ->putLong(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }.apply()
    }
}

