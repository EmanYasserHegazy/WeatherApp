package com.warba.core.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FullScreenCustomLoadingIndicator() {

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.secondary
    )

}

