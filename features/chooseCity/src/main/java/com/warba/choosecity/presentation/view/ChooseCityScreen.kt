package com.warba.choosecity.presentation.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.warba.core.ui.CustomTextInput
import com.warba.core.ui.SubmitButton

@Composable
fun ChooseCityScreen(context: Context, navHostController: NavHostController) {
    var inputValue by remember { mutableStateOf("") }
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .wrapContentSize(
                    Alignment.Center
                )
        ) {
            Text(
                text = "Choose a City",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = TextStyle(fontSize = 16.sp)
            )
            CustomTextInput(
                value = inputValue,
                onValueChange = { newValue -> inputValue = newValue },
                label = "City Name",
                KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))
            SubmitButton(buttonTitle = "View $inputValue Weather") {
                inputValue.takeIf { it.isNotBlank() }
                    ?.let { navHostController.navigate("currentWeatherRoute/$inputValue") }
                    ?: kotlin.run {
                        inputValue = ""
                        Toast.makeText(
                            context, "Please enter a city", Toast.LENGTH_SHORT
                        ).show()
                    }

            }

        }
    }
}


