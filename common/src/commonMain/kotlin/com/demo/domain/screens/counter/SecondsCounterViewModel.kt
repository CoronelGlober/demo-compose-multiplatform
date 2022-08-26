package com.demo.domain.screens.counter

import com.demo.expected.ViewModel
import com.demo.expected.backgroundDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class State(val timerStarted: Boolean = false, val seconds: Int = 0)

/**
 * Simple ViewModel for the seconds counter screen
 */
class SecondsCounterViewModel : ViewModel() {

    private val state: MutableStateFlow<State> = MutableStateFlow(State())
    val flowableState = state.asStateFlow()

    fun startTimer() {
        if (state.value.timerStarted) return

        state.value = State(true)
        viewModelScope.launch {
            println("Starting timer")
            withContext(backgroundDispatcher) {
                while (viewModelScope.coroutineContext.isActive) {
                    delay(1000)
                    println("setting a new second ${state.value.seconds}")
                    state.value = State(true, state.value.seconds + 1)
                }
            }
        }
    }
}
