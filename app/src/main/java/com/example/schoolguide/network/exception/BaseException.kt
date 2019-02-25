package com.example.schoolguide.network.exception

import java.lang.RuntimeException

open class BaseException : RuntimeException {

    var errorCode = HttpCode.CODE_UNKNOWN

     constructor() {

    }

    constructor(errorCode: Int, errorMessage: String) : super(errorMessage) {
        this.errorCode = errorCode
    }

}