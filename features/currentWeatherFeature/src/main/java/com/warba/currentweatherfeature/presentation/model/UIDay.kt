package com.warba.currentweatherfeature.presentation.model

data class UIDay(
    val datetime: String? = "",
    val tempMax: Double? = 0.0,
    val tempMin: Double? = 0.0,
    val description: String? = "",
    val dayWeatherState: String? = ""
)