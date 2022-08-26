package com.demo.ui.themes

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val LocalCommonColors = staticCompositionLocalOf<CommonColors> { error("No AlloyColors provided") }

val LocalRippleTheme = staticCompositionLocalOf<RippleTheme> { error("No AlloyColors provided") }

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}
