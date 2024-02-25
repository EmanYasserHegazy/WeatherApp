package com.warba.currentweatherfeature.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.warba.currentweatherfeature.domain.usecase.CurrentWeatherUseCase
import com.warba.currentweatherfeature.presentation.model.UIDay
import com.warba.currentweatherfeature.presentation.vm.CurrentWeatherViewModel
import com.warba.data.network.ErrorCodes
import com.warba.data.network.ResponsesResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrentWeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var weatherUseCase: CurrentWeatherUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        weatherUseCase = mockk()
        viewModel = CurrentWeatherViewModel(weatherUseCase)
    }

    @Test
    fun `fetchWeather updates currentWeatherState return Success`() {
        val cityName = "cairo"
        val mockResponse = listOf(UIDay("2024-02-25", 25.0, 15.0, "sunny", "sunny"))

        coEvery { weatherUseCase.invoke(cityName) } returns ResponsesResult.Success(mockResponse)

        val observer = mockk<Observer<ResponsesResult<List<UIDay>?>>>(relaxed = true)
        viewModel.currentWeatherState.observeForever(observer)


        viewModel.fetchWeather(cityName)


        verify {
            observer.onChanged(ResponsesResult.Loading)
            observer.onChanged(ResponsesResult.Success(mockResponse))
        }
    }

    @Test
    fun `fetchWeather updates currentWeatherState return failure`() {
        val cityName = "cairo"

        val errorCode = ErrorCodes.General("Error occurred", 500)
        coEvery { weatherUseCase.invoke(cityName) } returns ResponsesResult.Failure(errorCode)


        val observer = mockk<Observer<ResponsesResult<List<UIDay>?>>>(relaxed = true)
        viewModel.currentWeatherState.observeForever(observer)


        viewModel.fetchWeather(cityName)


        verify {
            observer.onChanged(ResponsesResult.Loading)
            observer.onChanged(ResponsesResult.Failure(errorCode))
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}
