package com.warba.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun CustomErrorDialog(
    dialogTitle: String,
    dialogDescription: String,
    icon: ImageVector,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(icon = { Icon(icon, contentDescription = null) },
            title = { Text(text = dialogTitle) },
            text = { Text(text = dialogDescription) },
            onDismissRequest = { onDismiss() },
            confirmButton = { TextButton(onClick = { onDismiss() }) { Text(text = "Dismiss") } })
    }
}