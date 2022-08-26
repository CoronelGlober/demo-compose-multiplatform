package com.demo.ui.themes

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.demo.expected.getAdditionalCompositions
import com.demo.expected.getPlatformColors


object CommonTheme {
    val colors: CommonColors
        @Composable
        get() = LocalCommonColors.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}


@Composable
fun CommonTheme(content: @Composable () -> Unit) {

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
