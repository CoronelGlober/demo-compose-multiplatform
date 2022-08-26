package com.demo.domain.screens.optionsscreen

import com.demo.data.network.client.ClientConfig
import com.demo.domain.screens.optionsscreen.MainScreenState.SharedChatRoom
import com.demo.domain.screens.optionsscreen.MainScreenState.SharedDrawingPad
import com.demo.expected.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed class MainScreenState {
    object OptionsScreen : MainScreenState()
    object SecondsCounter : MainScreenState()
    object SharedDrawingPad : MainScreenState()
    object SharedChatRoom : MainScreenState()
}

/**
 * Simple ViewModel for handling screen navigations
 */
class MainScreenViewModel : ViewModel() {
    private val state: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState.OptionsScreen)
    val flowableState = state.asStateFlow()

    val pendingAction: MutableStateFlow<(() -> Unit)?> = MutableStateFlow(null)

    var clearListener: (() -> Unit)? = null

    fun navigateToScreen(newScreen: MainScreenState) {
        if (
            (newScreen is SharedDrawingPad || newScreen is SharedChatRoom) &&
            (ClientConfig.ipAddress.isEmpty() || ClientConfig.port.isEmpty())
        ) {
            pendingAction.value = { this@MainScreenViewModel.navigateToScreen(newScreen) }
            return
        }
        clearListener?.invoke()
        clearListener = null
        state.value = newScreen
    }

    fun setCurrentClearListener(clearListener: () -> Unit) {
        this.clearListener = clearListener
    }

    fun dismissPendingAction() {
        this.pendingAction.value = null
    }
}
