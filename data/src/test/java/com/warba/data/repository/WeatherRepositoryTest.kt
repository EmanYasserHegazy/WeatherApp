package com.warba.data.repository

import com.warba.core.utils.NetworkUtil
import com.warba.data.model.remote.CurrentWeather
import com.warba.data.model.remote.Day
import com.warba.data.network.Constants
import com.warba.data.network.ErrorCodes
import com.warba.data.network.ResponsesResult
import com.warba.data.network.WeatherService
import com.warba.data.util.SharedPreferenceUtil
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherRepositoryTest {
    private lateinit var networkUtil: NetworkUtil
    private lateinit var weatherService: WeatherService
    private lateinit var sharedPreferencesUtil: SharedPreferenceUtil
    private lateinit var weatherRepository: WeatherRepositoryImpl

    @Before
    fun setup() {
        networkUtil = mockk()
        weatherService = mockk()
        sharedPreferencesUtil = mockk()

        weatherRepository =
            WeatherRepositoryImpl(weatherService, sharedPreferencesUtil, networkUtil)
    }

    @Test
    fun `test GetCurrentWeatherSuccess`() = runTest {
        val cityName = "cairo"
        val mockResponse: Response<CurrentWeather> = mockk()
        val mockCurrentWeather: CurrentWeather = mockk()
        val mockDayList: List<Day> = mockk()

        every { networkUtil.isNetworkConnected() } returns true
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code() } returns 200
        every { mockResponse.body() } returns mockCurrentWeather
        every { mockCurrentWeather.days } returns mockDayList
        coEvery { weatherService.getWeather(any(), any(), any(), any()) } returns mockResponse
        every { sharedPreferencesUtil.saveModelList(any(), any()) } just Runs

        val result = weatherRepository.getCurrentWeather(cityName)


        verify { networkUtil.isNetworkConnected() }
        coVerify { weatherService.getWeather(eq(cityName), any(), any(), any()) }


        assertEquals(ResponsesResult.Success(mockDayList), result)
    }

    @Test
    fun `test GetCurrentWeatherFailureWithNetworkIssue`() = runTest {
        val cityName = "cairo"
        val mockResponse: Response<CurrentWeather> = mockk()


        coEvery { weatherService.getWeather(any(), any(), any(), any()) } returns mockResponse
        every { networkUtil.isNetworkConnected() } returns false
        every { mockResponse.isSuccessful } returns false
        every { sharedPreferencesUtil.getModelList(any()) } returns null

        val result = weatherRepository.getCurrentWeather(cityName)
        val expectedError =
            ErrorCodes.NetworkIssue(Constants.NOT_CONNECTED_TO_NETWORK_ERROR_MESSAGE)


        assertTrue(result is ResponsesResult.Failure)
        assertEquals(expectedError.message, (result as ResponsesResult.Failure).error.message)
        assertEquals(expectedError.code, result.error.code)

    }

    @Test
    fun `test GetOfflineEmptyCurrentWeatherFromCacheFail`() = runTest {
        val cityName = "cairo"
        every { networkUtil.isNetworkConnected() } returns false
        every { sharedPreferencesUtil.getModelList(any()) } returns emptyList()
        coEvery {
            weatherService.getWeather(
                any(), any(), any(), any()
            )
        } returns Response.success(null)

        val result = weatherRepository.getCurrentWeather(cityName)

        verify { networkUtil.isNetworkConnected() }
        assertTrue(result is ResponsesResult.Failure)
    }

    @Test
    fun `test GetOfflineCurrentWeatherFromCacheSuccess`() = runTest {
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
        every { networkUtil.isNetworkConnected() } returns false
        every { sharedPreferencesUtil.getModelList(any()) } returns days
        coEvery {
            weatherService.getWeather(
                any(), any(), any(), any()
            )
        } returns Response.success(null)

        val result = weatherRepository.getCurrentWeather(cityName)

        verify { networkUtil.isNetworkConnected() }
        assertTrue(result is ResponsesResult.Success)
    }

    @Test
    fun `test GetCurrentWeatherThrowsHttpException`() = runTest {
        val cityName = "cairo"
        val mockResponse: Response<CurrentWeather> = mockk()
        val expectedError =
            ErrorCodes.ServerError(
                Constants.SERVER_ERROR_MESSAGE, Constants.ERROR_CODE_403
            )
        every { networkUtil.isNetworkConnected() } returns true
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns Constants.ERROR_CODE_403
        every { mockResponse.message() } returns Constants.SERVER_ERROR_MESSAGE
        coEvery {
            weatherService.getWeather(
                any(),
                any(),
                any(),
                any()
            )
        }.throws(HttpException(mockResponse))


        val result = weatherRepository.getCurrentWeather(cityName)

        assertTrue(result is ResponsesResult.Failure)
        assertEquals(expectedError.message, (result as ResponsesResult.Failure).error.message)
        assertEquals(expectedError.code, result.error.code)
    }

    @Test
    fun `test GetCurrentWeatherThrowsIOException`() = runTest {
        val cityName = "cairo"
        val mockResponse: Response<CurrentWeather> = mockk()
        val expectedError =
            ErrorCodes.IOExceptionError(
                Constants.IO_Exception_ERROR_MESSAGE, Constants.ERROR_CODE_400
            )
        every { networkUtil.isNetworkConnected() } returns true
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns Constants.ERROR_CODE_400
        every { mockResponse.message() } returns Constants.IO_Exception_ERROR_MESSAGE
        coEvery {
            weatherService.getWeather(
                any(),
                any(),
                any(),
                any()
            )
        }.throws(IOException(anyString()))


        val result = weatherRepository.getCurrentWeather(cityName)

        assertTrue(result is ResponsesResult.Failure)
        assertEquals(expectedError.message, (result as ResponsesResult.Failure).error.message)
        assertEquals(expectedError.code, result.error.code)
    }


    @After
    fun tearDown() {
        clearAllMocks()
    }
}