package com.example.schoolguide

import android.app.Application
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        appContext = this
    }

    companion object {
        var instance: App by Delegates.notNull()

        @JvmStatic
        private var appContext: Application? = null

        @JvmStatic
        fun getAppContext(): Application? = appContext
    }
}