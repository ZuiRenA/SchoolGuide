package com.example.schoolguide

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        @JvmStatic
        private var appContext: Application? = null

        @JvmStatic
        fun getAppContext(): Application? = appContext
    }
}