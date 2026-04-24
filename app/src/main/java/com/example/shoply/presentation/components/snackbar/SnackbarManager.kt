package com.example.shoply.presentation.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackbarManager {

    private val _events = MutableSharedFlow<SnackbarEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val events = _events.asSharedFlow()

    fun showSnackbar(
        snackbarEvent: SnackbarEvent
    ) {
        _events.tryEmit(snackbarEvent)
    }

    data class SnackbarEvent(
        override val duration: SnackbarDuration,
        override val withDismissAction: Boolean,
        override val actionLabel: String? = null,
        override val message: String,
        val type: SnackbarType
    ) : SnackbarVisuals

    enum class SnackbarType {
        SUCCESS,
        ERROR,
        INFO
    }
}