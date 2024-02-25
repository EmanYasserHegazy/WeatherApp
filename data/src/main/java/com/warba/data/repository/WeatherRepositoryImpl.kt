package com.warba.data.repository

import com.warba.core.utils.NetworkUtil
import com.warba.data.model.remote.Day
import com.warba.data.network.Constants
import com.warba.data.network.ErrorCodes
import com.warba.data.network.ResponsesResult
import com.warba.data.network.WeatherService
import com.warba.data.util.SharedPreferenceUtil
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val sharedPreferencesUtil: SharedPreferenceUtil,
    private val networkUtil: NetworkUtil
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        cityName: String
    ): ResponsesResult<List<Day>?> {
        return if (networkUtil.isNetworkConnected()) {
            try {
                weatherService.getWeather(
                    cityName, Constants.UNIT_GROUP, Constants.API_KEY, "json"
                ).let { response ->
                    if (response.isSuccessful && response.code() == Constants.SUCCESS_CODE_200) {
                        val weatherData = response.body()

                        weatherData?.days?.also { days ->
                            sharedPreferencesUtil.saveModelList(
                                Constants.CURRENT_WEATHER_SHARED_PREFERENCE_KEY, days
                            )
                        }
                        ResponsesResult.Success(weatherData?.days)

                    } else {
                        ResponsesResult.Failure(
                            ErrorCodes.General(
                                Constants.GENERAL_ERROR_MESSAGE, Constants.ERROR_CODE_500
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                handleFetchWeatherDataFailure(e)
            }
        } else {
            sharedPreferencesUtil.getModelList(Constants.CURRENT_WEATHER_SHARED_PREFERENCE_KEY)
                ?.takeIf { it.isNotEmpty() }?.let { ResponsesResult.Success(it) }
                ?: ResponsesResult.Failure(
                    ErrorCodes.NetworkIssue(
                        Constants.NOT_CONNECTED_TO_NETWORK_ERROR_MESSAGE
                    )
                )
        }
    }


    // if i build a big project or we can consider it for the future work i will handle exceptions and errors using strategy design pattern in seperated manner i think its better choice to use here, beacuse here i violate the O/C and might be violate single responsibility in SOLID but it is due to time Constraints
    private fun handleFetchWeatherDataFailure(
        e: Exception
    ): ResponsesResult.Failure {
        return when (e) {
            is IOException -> {
                ResponsesResult.Failure(
                    ErrorCodes.IOExceptionError(
                        Constants.IO_Exception_ERROR_MESSAGE, Constants.ERROR_CODE_400
                    )
                )
            }

            is HttpException -> ResponsesResult.Failure(
                ErrorCodes.ServerError(
                    Constants.SERVER_ERROR_MESSAGE, Constants.ERROR_CODE_403
                )
            )

            else -> ResponsesResult.Failure(
                ErrorCodes.General(
                    Constants.GENERAL_ERROR_MESSAGE, Constants.ERROR_CODE_500
                )
            )
        }

    }
}




