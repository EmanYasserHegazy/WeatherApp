package com.warba.weatherapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.warba.choosecity.presentation.view.ChooseCityScreen
import com.warba.currentweatherfeature.presentation.view.CurrentWeatherScreen

@Composable
fun AppNavigation(context: Context, navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.City().route) {
        composable(NavigationItem.City().screenRoute) {
           ChooseCityScreen(context, navController)
        }
        composable(
            route = NavigationItem.CurrentWeather("cairo").screenRoute + "/{cityName}",
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            CurrentWeatherScreen(
                navController, cityName
            )
        }
    }
}

