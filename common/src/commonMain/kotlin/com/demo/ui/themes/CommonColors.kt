package com.demo.ui.themes

import androidx.compose.material.Colors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Stable
class CommonColors(
    primary: Color,
    primaryVariant: Color,
    onPrimary: Color,
    secondary: Color,
    onSecondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    onSurface: Color,
    error: Color,
    onError: Color,
    onBackground: Color,
) {

    var primary by mutableStateOf(primary)
        private set
    var primaryVariant by mutableStateOf(primaryVariant)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var secondaryVariant by mutableStateOf(secondaryVariant)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var surface by mutableStateOf(surface)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var error by mutableStateOf(error)
        private set
    var onError by mutableStateOf(onError)
        private set

    fun update(other: CommonColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
    }
}


@Composable
fun getParsedPlatformColors(darkTheme: Boolean) = Colors(
    primary = CommonTheme.colors.primary,
    primaryVariant = CommonTheme.colors.primaryVariant,
    onPrimary = CommonTheme.colors.onPrimary,
    secondary = CommonTheme.colors.secondary,
    secondaryVariant = CommonTheme.colors.secondaryVariant,
    onSecondary = CommonTheme.colors.onSecondary,
    background = CommonTheme.colors.background,
    onBackground = CommonTheme.colors.onBackground,
    surface = CommonTheme.colors.surface,
    onSurface = CommonTheme.colors.onSurface,
    error = CommonTheme.colors.error,
    onError = CommonTheme.colors.onError,
    isLight = !darkTheme
)
