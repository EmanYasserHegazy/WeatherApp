package com.warba.currentweatherfeature.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warba.currentweatherfeature.domain.usecase.CurrentWeatherUseCase
import com.warba.currentweatherfeature.presentation.model.UIDay
import com.warba.data.network.ResponsesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(private val weatherUseCase: CurrentWeatherUseCase) :
    ViewModel() {

    private val _currentWeatherState = MutableLiveData<ResponsesResult<List<UIDay>?>>()
    val currentWeatherState: LiveData<ResponsesResult<List<UIDay>?>> = _currentWeatherState

    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            _currentWeatherState.value = ResponsesResult.Loading
            _currentWeatherState.value = fetchCurrentWeather(cityName)
        }

    }

    private suspend fun fetchCurrentWeather(cityName: String) = weatherUseCase.invoke(
        cityName
    )

}