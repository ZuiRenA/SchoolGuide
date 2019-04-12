package com.example.schoolguide.util

import com.example.schoolguide.model.User
import com.tencent.mmkv.MMKV

object LoginUtil {

    private const val User_Info = "user_info"
    private const val User_Account = "user_account"
    private const val User_Password = "user_password"
    const val Null_String = ""

    var user: User ?= null

    data class UserInfo(
        var account: String,
        var password: String
    )

    private val kv = MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, User_Info)

    fun saveLoginInfo(account: String?, password: String?) {
        kv.encode(User_Account, account)
        kv.encode(User_Password, password)
    }

    fun getLoginInfo(): UserInfo =
        UserInfo(kv.decodeString(User_Account, Null_String), kv.decodeString(User_Password, Null_String))

    fun clearLoginInfo() {
        kv.clearAll()
    }
}