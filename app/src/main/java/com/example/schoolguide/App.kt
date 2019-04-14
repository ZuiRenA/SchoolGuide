package com.example.schoolguide

import android.app.Application
import android.content.ContextWrapper
import com.google.firebase.storage.FirebaseStorage
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

private lateinit var appContext: Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        appContext = this
    }
}

object AppContext: ContextWrapper(appContext)