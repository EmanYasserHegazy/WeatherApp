package com.warba.currentweatherfeature.presentation.model


/*
 *Model class representing Day for UI display
 **@datetime represents the data and time of the day
 ***@tempMax represents the max temperature of the day
 *****@tempMin represents the min temperature of the day
 * ****@description represents day temperature's description
 * ****@dayWeatherState represents the state of the day temperature ie.cloudy
 */
data class UIDay(
    val datetime: String? = "",
    val tempMax: Double? = 0.0,
    val tempMin: Double? = 0.0,
    val description: String? = "",
    val dayWeatherState: String? = ""
)