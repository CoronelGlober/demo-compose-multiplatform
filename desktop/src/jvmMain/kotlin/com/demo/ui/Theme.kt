package com.demo.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.demo.expected.getAdditionalCompositions
import com.demo.expected.getPlatformColors
import com.demo.ui.themes.LocalCommonColors
import com.demo.ui.themes.getParsedPlatformColors

@Composable
internal fun DesktopTheme(content: @Composable () -> Unit) {

    val colors = getPlatformColors()
    CompositionLocalProvider(LocalCommonColors provides colors) {
        MaterialTheme(
            colors = getParsedPlatformColors(false),
            typography = Typography(),
            shapes = Shapes()
        ) {
            CompositionLocalProvider(
                *getAdditionalCompositions().toTypedArray(),
                content = content
            )
        }
    }
}