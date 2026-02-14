package com.example.shoply.presentation.components.dialogs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DialogHost {

    private val _state = MutableStateFlow<DialogState>(DialogState.None)
    val state: StateFlow<DialogState> = _state.asStateFlow()

    fun showDialog(type: DialogState) {
        _state.update { type }
    }

    fun dismissDialog() {
        _state.update { DialogState.None }
    }

}

// Dialog -> Wondering about pass Dialog into the manager
