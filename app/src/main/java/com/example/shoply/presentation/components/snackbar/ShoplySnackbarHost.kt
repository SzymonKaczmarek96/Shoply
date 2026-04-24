package com.example.shoply.presentation.components.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShoplySnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        SnackbarManager.events.collectLatest { event ->
            hostState.showSnackbar(event)
        }
    }

    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        val visuals = data.visuals as? SnackbarManager.SnackbarEvent

        val backgroundColor = when (visuals?.type) {
            SnackbarManager.SnackbarType.SUCCESS -> Color(0xFF2E7D32)
            SnackbarManager.SnackbarType.ERROR -> Color(0xFFD32F2F)
            SnackbarManager.SnackbarType.INFO -> Color(0xFF0288D1)
            null -> MaterialTheme.colorScheme.surfaceVariant
        }

        val icon = when (visuals?.type) {
            SnackbarManager.SnackbarType.SUCCESS -> Icons.Default.CheckCircle
            SnackbarManager.SnackbarType.ERROR -> Icons.Default.Error
            SnackbarManager.SnackbarType.INFO -> Icons.Default.Info
            null -> null
        }

        Snackbar(
            modifier = Modifier.padding(12.dp),
            containerColor = backgroundColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp),
            action = if (!data.visuals.actionLabel.isNullOrEmpty()) {
                {
                    TextButton(
                        onClick = { data.performAction() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text(data.visuals.actionLabel ?: "")
                    }
                }
            } else null,
            dismissAction = if (data.visuals.withDismissAction) {
                {
                    IconButton(
                        onClick = { data.dismiss() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            } else null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(12.dp))
                }
                Text(
                    text = data.visuals.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}