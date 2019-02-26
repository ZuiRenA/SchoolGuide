package com.example.schoolguide.network

import android.content.Context
import android.net.wifi.WifiManager
import com.tencent.mmkv.MMKV

object DeviceInfo {
    private const val UUID_INFO = "uuid_info"
    private const val UUID = "uuid"

    fun unique_sign(context: Context): String {
        var macAddress: String? = null
        val wifiMgr = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiMgr.connectionInfo
        if (null != info) {
            macAddress = info.macAddress
        }
        return if (macAddress == null) {
            "no_mac_address"
        } else {
            MD5Util.encode(macAddress)
        }
    }

    fun UUID(): String? {
        val mmkvUUID = MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, UUID_INFO)
        val uuid = mmkvUUID.decodeString(UUID, null)

        val randomUUID = java.util.UUID.randomUUID().toString()
        if (uuid == null) {
            mmkvUUID.encode(UUID, randomUUID)
        }

        return uuid?: randomUUID
    }
}