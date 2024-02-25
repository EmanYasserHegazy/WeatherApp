package com.warba.data.network


sealed class ResponsesResult<out T> {
    data class Success<out T>(val data: T) : ResponsesResult<T>()
    data class Failure(val error: ErrorCodes) : ResponsesResult<Nothing>()
    object Loading : ResponsesResult<Nothing>()
}