package com.warba.weatherapp.navigation

sealed class NavigationItem(val screenTitle: String, val screenRoute: String) {
    data class City(val title: String = "Choose City", val route: String = "citySelectionRoute") :
        NavigationItem(title, route)

    data class CurrentWeather(
        val cityName: String,
        val title: String = "Weather Details$cityName",
        val route: String = "currentWeatherRoute"
    ) : NavigationItem(title, route)
}


