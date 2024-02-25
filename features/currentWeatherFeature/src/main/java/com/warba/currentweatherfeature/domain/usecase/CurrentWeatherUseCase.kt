package com.warba.currentweatherfeature.domain.usecase

import com.warba.currentweatherfeature.presentation.model.UIDay
import com.warba.data.model.remote.Day
import com.warba.data.network.Constants
import com.warba.data.network.ErrorCodes
import com.warba.data.network.ResponsesResult
import com.warba.data.repository.WeatherRepository
import javax.inject.Inject

const val WEATHER_FORECAST_DAYS = 7

class CurrentWeatherUseCase @Inject constructor(
    private val currentWeatherRepository: WeatherRepository
) {

    suspend fun invoke(
        cityName: String
    ): ResponsesResult<List<UIDay>?> {

        val currentWeatherList: ResponsesResult<List<Day>?> =
            currentWeatherRepository.getCurrentWeather(cityName)
        return currentWeatherList.mapToUIDayList()

    }

    private fun ResponsesResult<List<Day>?>.mapToUIDayList(): ResponsesResult<List<UIDay>?> {
        return when (this) {
            is ResponsesResult.Success -> {
                val uiDayList = this.data?.map { day ->
                    UIDay(
                        datetime = day.datetime,
                        tempMax = day.tempMax,
                        tempMin = day.tempMin,
                        description = day.description,
                        dayWeatherState = day.dayWeatherState
                    )
                }?.take(WEATHER_FORECAST_DAYS)
                ResponsesResult.Success(uiDayList)
            }

            is ResponsesResult.Failure -> {

                ResponsesResult.Failure(this.error)
            }

            else -> {
                ResponsesResult.Failure(
                    ErrorCodes.General(
                        "Unexpected error occurred", Constants.ERROR_CODE_500
                    )
                )
            }
        }
    }
}


