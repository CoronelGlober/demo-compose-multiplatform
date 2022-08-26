package com.demo.expected

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ButtonColors
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.demo.ui.themes.CommonColors
import com.demo.ui.themes.LocalCommonColors
import com.demo.ui.utils.enlightenColor

actual fun getPlatformColors() = CommonColors(
    primary = Color(0XFF44484a),
    primaryVariant = Color(0xFF3325C9),
    secondary = Color(0xFFC5C3EB),
    secondaryVariant = Color(0xFF292b2d),
    background = Color(0XFF44484a),
    surface = Color(0xFF4c4e50),
    error = Color(0xFFFFB4AB),
    onPrimary = Color(0XFFFFFFFF),
    onSecondary = Color(0XFF2E2D4D),
    onBackground = Color(0XFF606263).enlightenColor(0.9f),
    onSurface = Color(0XFFE5E1E6),
    onError = Color(0XFF690005)
)

actual fun getAdditionalCompositions(): List<ProvidedValue<*>> = emptyList()

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PlatformButton(
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    TextButton(onClick = onClick,
        colors = object : ButtonColors {

            @Composable
            override fun backgroundColor(enabled: Boolean): State<Color> {
                return rememberUpdatedState(if (hovered) LocalCommonColors.current.surface else LocalCommonColors.current.onBackground)
            }

            @Composable
            override fun contentColor(enabled: Boolean): State<Color> {
                return rememberUpdatedState(LocalCommonColors.current.onPrimary)
            }
        },
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) { hovered = true }
            .onPointerEvent(PointerEventType.Exit) { hovered = false },
        content = content,
        enabled = enabled
    )
}

@Composable
actual fun getPlatformBackArrow() {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "Back"
    )
}

actual val platformDrawingStroke: Float = 3f

actual fun getPlatformChatToolbarColor(): Color  = Color(0xFF00897B)

actual val platformBottomSpacing = 0.dp