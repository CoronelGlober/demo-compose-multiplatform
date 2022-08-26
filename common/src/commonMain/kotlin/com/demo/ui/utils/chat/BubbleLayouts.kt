package com.demo.ui.utils.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BubbleLayout(
    modifier: Modifier = Modifier,
    bubbleState: BubbleState,
    content: @Composable () -> Unit
) {
    Column(
        modifier
            .drawBubble(bubbleState)
    ) {
        content()
    }
}

@Composable
fun BubbleLayoutWithShape(
    modifier: Modifier = Modifier,
    bubbleState: BubbleState,
    content: @Composable () -> Unit
) {

    Column(
        modifier.drawBubbleWithShape(bubbleState)
    ) {
        content()
    }
}