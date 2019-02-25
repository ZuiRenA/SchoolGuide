package com.example.schoolguide.network.exception

class ParamterInvalidException: BaseException(HttpCode.CODE_PARAMETER_INVALID, "参数有误")

class TokenInvalidException : BaseException(HttpCode.CODE_TOKEN_INVALID, "Token失效")

class AccountInvalidException: BaseException(HttpCode.CODE_ACCOUNT_INVALID, "账号或者密码错误")

class ConnectionException : BaseException(HttpCode.CODE_CONNECTION_FAILED, "网络请求失败")

class ForbiddenException : BaseException(HttpCode.CODE_PARAMETER_INVALID, "404错误")

class ResultInvalidException : BaseException(HttpCode.CODE_RESULT_INVALID, "无效请求")

class ServerResultException(code: Int, message: String) : BaseException(code, message)