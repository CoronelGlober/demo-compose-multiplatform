package com.demo.expected

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.demo.ui.themes.CommonColors

actual fun getPlatformColors() = CommonColors(
    primary = Color(0xFF435C9C),
    primaryVariant = Color(0xFFDAE2FF),
    secondary = Color(0xFF595E6E),
    secondaryVariant = Color(0xFFDEE2F5),
    background = Color.White,
    surface = Color(0xFFFEFBFF),
    error = Color(0xFFBA1A1A),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.Black,
    onError = Color.White
)

actual fun getAdditionalCompositions(): List<ProvidedValue<*>> = emptyList()

@Composable
actual fun PlatformButton(
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) = Button(onClick = onClick, content = content, enabled = enabled)

@Composable
actual fun getPlatformBackArrow()
{
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "Back"
    )
}

actual val platformDrawingStroke: Float = 10f
actual fun getPlatformChatToolbarColor(): Color  = Color(0xFF00897B)

actual val platformBottomSpacing = 20.dp