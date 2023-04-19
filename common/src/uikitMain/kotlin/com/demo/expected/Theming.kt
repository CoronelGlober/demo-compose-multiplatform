package com.demo.expected

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.demo.ui.themes.CommonColors
import com.demo.ui.themes.LocalCommonColors
import com.demo.ui.themes.NoRippleTheme
import com.demo.ui.utils.chat.IosArrow

actual fun getPlatformColors() = CommonColors(
    primary = Color(0xFFF8F8F8),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFFEFEFD8),
    background = Color.White,
    surface = Color(0xFF0085ff),
    error = Color(0xFFB00020),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color(0xFFFF0000),
    onError = Color.White
)

internal actual fun getAdditionalCompositions(): List<ProvidedValue<*>> = listOf(LocalRippleTheme provides NoRippleTheme)

@Composable
actual fun PlatformButton(
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) = OutlinedButton(onClick = onClick, content = content, enabled = enabled)

@Composable
actual fun getPlatformBackArrow() {
    Row(modifier = Modifier.fillMaxHeight().padding(start = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = Icons.Filled.IosArrow,
            contentDescription = "Back",
            colorFilter = ColorFilter.tint(LocalCommonColors.current.surface)
        )
        Text("Back", color = LocalCommonColors.current.surface)
    }
}

actual val platformDrawingStroke: Float = 3f
@Composable
actual fun getPlatformChatToolbarColor(): Color  = LocalCommonColors.current.primary

// iOS needs an additional bottom spacing because of its divider line
actual val platformBottomSpacing = 20.dp