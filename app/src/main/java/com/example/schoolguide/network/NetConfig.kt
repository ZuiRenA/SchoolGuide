package com.example.schoolguide.network

object NetConfig {
    const val SCHEMEL = "https"
    const val HOST = "api-test.enbeedu.com"

    const val BASE_URL = "https://api-test.enbeedu.com"


    const val FORMAT_KEY = "Content-Type"
    const val FORMAT_VALUE = "application/json; charset=utf-8"


    // ## app key
    const val APPKEY_KEY = "HTTP_X_REST_APPKEY"
    const val APPKEY_KEY_INQUERY = "app_key"

    const val TEST_APPKEY_VALUE = "yx-test"
    const val APPKEY_VALUE = "yx-ios"

    // ## sign
    const val SIGN_KEY = "HTTP_X_REST_APPSIGN"
    const val TEST_SIGN_VALUE = "123456"
    const val SIGN_VALUE = "5buxC06oGN1ayaEcMHHgd0XIm30XBur1"


    // ## session
    const val SESSION_KEY = "TOP_SESSION"

    // ## device info
    const val APPVERSIONNAME_KEY = "app_v_name"
    const val APPVERSIONCODE_KEY = "app_v_code"

    const val OS_KEY = "os"
    const val OS_VAlUE = "android"
    const val CHANNEL_KEY = "channel"
    const val CHANNEL_VALUE = "appstore"
    // # QUERYs

    // ## api version
    const val VERSION_KEY = "v"
    const val VERSION_VALUE = "1.0"

    // ## uuid
    const val UUID_KEY = "UUID"

    // ## timestamp
    const val TIMESTAMP_KEY = "timestamp"

    // ## useful ???
    const val FIELDS_VERSION_KEY = "fields_version"
    const val FIELDS_VERSION_VALUE = "1.0"
    const val ACCEPT_LANGUAGE = "Accept-Language"
}