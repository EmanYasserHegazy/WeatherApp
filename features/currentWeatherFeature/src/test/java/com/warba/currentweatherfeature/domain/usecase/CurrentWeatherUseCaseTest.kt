package com.warba.currentweatherfeature.domain.usecase

import com.warba.currentweatherfeature.presentation.model.UIDay
import com.warba.data.model.remote.CurrentWeather
import com.warba.data.model.remote.Day
import com.warba.data.network.ResponsesResult
import com.warba.data.repository.WeatherRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrentWeatherUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var currentWeatherUseCase: CurrentWeatherUseCase

    @Before
    fun setup() {
        weatherRepository = mockk()
        currentWeatherUseCase = CurrentWeatherUseCase(weatherRepository)
    }

    @Test
    fun `invoke api with valid city name should return success`() = runTest {
        val cityName = "cairo"
        val days = listOf(
            Day(
                datetime = "2024-02-25",
                datetimeEpoch = 0,
                tempMax = 25.0,
                tempMin = 15.0,
                description = "sunny",
                dayWeatherState = "clear"
            ), Day(
                datetime = "2024-02-26",
                datetimeEpoch = 0,
                tempMax = 20.0,
                tempMin = 10.0,
                description = "cloudy",
                dayWeatherState = "cloudy"
            )
        )

        val mockResponse: ResponsesResult<List<Day>> = ResponsesResult.Success(days)
        val currentWeather = CurrentWeather(days = days)
        coEvery { weatherRepository.getCurrentWeather(cityName) } returns mockResponse

        val result = currentWeatherUseCase.invoke(cityName = cityName)

        val expectedUIDays = days.map {
            UIDay(
                datetime = it.datetime,
                tempMax = it.tempMax,
                tempMin = it.tempMin,
                description = it.description,
                dayWeatherState = it.dayWeatherState
            )
        }
        assertEquals(result, ResponsesResult.Success(expectedUIDays))

    }


    @After
    fun tearDown() {
        clearAllMocks()
    }
}