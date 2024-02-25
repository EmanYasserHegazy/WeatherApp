package com.warba.currentweatherfeature.presentation.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.warba.core.ui.CustomErrorDialog
import com.warba.core.ui.CustomTempCard
import com.warba.core.ui.FullScreenCustomLoadingIndicator
import com.warba.core.utils.DateUtil
import com.warba.currentweatherfeature.presentation.model.UIDay
import com.warba.currentweatherfeature.presentation.vm.CurrentWeatherViewModel
import com.warba.data.network.ResponsesResult


@Composable
fun CurrentWeatherScreen(
    navController: NavController,
    cityName: String,
    currentWeatherViewModel: CurrentWeatherViewModel = hiltViewModel()
) {

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            var currentWeatherState by remember {
                mutableStateOf<ResponsesResult<List<UIDay>?>>(ResponsesResult.Loading)
            }

            LaunchedEffect(cityName) {
                currentWeatherViewModel.fetchWeather(cityName)
            }

            currentWeatherViewModel.currentWeatherState.observeAsState().value?.let { currentState ->
                currentWeatherState = currentState
            }

            when (currentWeatherState) {
                is ResponsesResult.Loading -> {
                    FullScreenCustomLoadingIndicator()
                }

                is ResponsesResult.Success -> {
                    val daysList = (currentWeatherState as ResponsesResult.Success).data

                    daysList?.let { daysList ->
                        if (daysList.isNotEmpty()) {
                            RenderWeatherView(cityName = cityName, days = daysList)
                        }
                    } ?: EmptyWeatherView(cityName, navController)
                }

                is ResponsesResult.Failure -> {
                    val error = (currentWeatherState as ResponsesResult.Failure).error
                    val showErrorDialogState = remember { mutableStateOf(true) }
                    CustomErrorDialog("An error has been occurred",
                        error.message,
                        Icons.Default.Close,
                        showErrorDialogState.value,
                        onDismiss = {
                            showErrorDialogState.value = false
                            navController.navigateUp()

                        })

                }
            }

        }
    }
}

@Composable
fun EmptyWeatherView(cityName: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .wrapContentSize(
                Alignment.Center
            )
    ) {
        Text(
            text = "No weather data found for the city $cityName please Try another city name ",
            style = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center)
        )
        Button(onClick = { navController.navigateUp() }) {
            Text(text = "Go Back")
        }
    }
}

@Composable
fun RenderWeatherView(cityName: String, days: List<UIDay>?) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomTempCard {
            OneDayCityWeather(cityName, days?.get(0))
        }
        Spacer(modifier = Modifier.height(30.dp))

        CustomTempCard {
            WeatherForecastSevenDays(days = days)

        }

    }
}

@Composable
fun WeatherForecastSevenDays(
    days: List<UIDay>?
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        days?.let { list ->
            items(list) { day ->
                WeatherListItem(
                    (day.tempMin ?: "").toString(), (day.tempMax ?: "").toString(), day.datetime
                )
            }
        }
    }
}

@Composable
fun OneDayCityWeather(cityName: String, today: UIDay?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(
                Alignment.TopCenter

            )

    ) {
        Text(
            text = (today?.tempMax ?: "").toString(), style = TextStyle(
                fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
            ), modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.padding(bottom = 6.dp)) {
            Text(
                text = (cityName), style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = (today?.dayWeatherState ?: ""),
                style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.End)
            )
        }
    }
}

//TODO
@Composable
fun WeatherListItem(minTemp: String, maxTemp: String, dayTime: String?, util: DateUtil = DateUtil) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val dayMonth = util.extractDayAndMonth(dayTime ?: "")
        Text(
            text = "$minTemp/$maxTemp",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
           // verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = util.getWeekDayName(dayTime ?: ""), style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${dayMonth.first} / ${dayMonth.second}",
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}