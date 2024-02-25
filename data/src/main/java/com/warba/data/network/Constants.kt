package com.warba.data.network

object Constants {
    const val BASE_URL =
        "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/"
    const val API_KEY = "9BTKN4JEESSQJFT6YYFHZYLBA"
    const val TIMEOUT_CONNECT = 60
    const val TIMEOUT_READ = 60
    const val CACHE_SIZE = 5 * 1024 * 1024
    const val CONTENT_TYPE = "json"
    const val UNIT_GROUP ="metric"
    const val GENERAL_ERROR_MESSAGE = "General error has been occurred, bad request"
    const val SERVER_ERROR_MESSAGE = "Server error has been occurred"
    const val CURRENT_WEATHER_SHARED_PREFERENCE_KEY = "LastSearchedCity"
    const val AUTHENTICATION_ERROR_MESSAGE = "Authentication error has been occurred"
    const val NOT_CONNECTED_TO_NETWORK_ERROR_MESSAGE = "Please connect to the wifi or mobile data"
    const val IO_Exception_ERROR_MESSAGE = "Forbidden,Bad Request"
    const val ERROR_CODE_401 = 401
    const val SUCCESS_CODE_200 = 200
    const val ERROR_CODE_500 = 500
    const val ERROR_CODE_400 = 400
    const val ERROR_CODE_403 = 403 // dummy code for now
    const val OK = 200
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val TOO_MANY_REQUESTS = 429
    const val INTERNAL_SERVER_ERROR = 500
}