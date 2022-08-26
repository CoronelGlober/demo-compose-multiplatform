package com.demo.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.demo.domain.screens.counter.SecondsCounterViewModel
import com.demo.expected.PlatformButton

/**
 * Basic seconds counter UI definition
 */
@Composable
fun SecondsCounter(viewModel: SecondsCounterViewModel = SecondsCounterViewModel()) {

    val state = viewModel.flowableState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PlatformButton(enabled = true, onClick = { viewModel.startTimer() }) {
            Text(if (state.value.timerStarted) "Counting!! -> ${state.value.seconds}" else "Start Timer!")
        }
    }
}
