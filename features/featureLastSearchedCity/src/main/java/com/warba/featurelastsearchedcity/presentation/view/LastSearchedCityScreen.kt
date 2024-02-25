package com.warba.featurelastsearchedcity.presentation.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.warba.featurelastsearchedcity.presentation.vm.LastSearchedCityViewModel

@Composable
fun LastSearchedCityScreen(
    navController: NavController,
    cityName: String,
    currentWeatherViewModel: LastSearchedCityViewModel = hiltViewModel()
) {

}