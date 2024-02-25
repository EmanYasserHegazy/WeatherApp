package com.warba.data.repository

import com.warba.data.model.remote.Day
import com.warba.data.network.ResponsesResult

interface WeatherRepository {
    suspend fun getCurrentWeather(
        cityName: String
    ): ResponsesResult<List<Day>?>
}
