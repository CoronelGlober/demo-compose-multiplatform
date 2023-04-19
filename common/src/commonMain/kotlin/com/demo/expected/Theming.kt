package com.demo.expected

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.demo.ui.themes.CommonColors

expect fun getPlatformColors(): CommonColors

internal expect fun getAdditionalCompositions(): List<ProvidedValue<*>>

@Composable
expect fun PlatformButton(
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
)

@Composable
expect fun getPlatformBackArrow()

expect fun getPlatformChatToolbarColor() : Color

expect val platformDrawingStroke: Float

expect val platformBottomSpacing: Dp