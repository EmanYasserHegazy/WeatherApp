package com.warba.data.network

sealed class ErrorCodes(val message: String, val code: Int?=0) {
    class ServerError(message: String, code: Int) : ErrorCodes(message, code)
    class AuthenticationError(message: String, code: Int) : ErrorCodes(message, code)
    class IOExceptionError(message: String, code: Int) : ErrorCodes(message, code)
    class General(message: String, code: Int) : ErrorCodes(message, code)
    class NetworkIssue(message: String) : ErrorCodes(message)
}
