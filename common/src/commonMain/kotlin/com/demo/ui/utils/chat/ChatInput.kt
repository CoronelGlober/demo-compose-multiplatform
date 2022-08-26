package com.demo.ui.utils.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.ui.themes.CommonColors
import com.demo.ui.themes.LocalCommonColors

private fun getDefaultMaterialColors(): CommonColors {
    return CommonColors(
        primary = Color(0xFF435C9C),
        primaryVariant = Color(0xFFDAE2FF),
        secondary = Color(0xFF595E6E),
        secondaryVariant = Color(0xFFDEE2F5),
        background = Color.White,
        surface = Color(0xFFFEFBFF),
        error = Color(0xFFBA1A1A),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        onError = Color.White
    )
}

fun getParsedMaterialColors(colors: CommonColors) = Colors(
    primary = colors.primary,
    primaryVariant = colors.primaryVariant,
    onPrimary = colors.onPrimary,
    secondary = colors.secondary,
    secondaryVariant = colors.secondaryVariant,
    onSecondary = colors.onSecondary,
    background = colors.background,
    onBackground = colors.onBackground,
    surface = colors.surface,
    onSurface = colors.onSurface,
    error = colors.error,
    onError = colors.onError,
    isLight = true
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ChatInput(modifier: Modifier = Modifier, onMessageChange: (String) -> Unit) {

    var input by remember { mutableStateOf(TextFieldValue("")) }
    val textEmpty: Boolean by derivedStateOf { input.text.isEmpty() }

    val colors = getDefaultMaterialColors()
    CompositionLocalProvider(LocalCommonColors provides colors) {
        MaterialTheme(
            colors = getParsedMaterialColors(colors),
            typography = Typography(),
            shapes = Shapes()
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {

                ChatTextField(
                    modifier = modifier.weight(1f),
                    input = input,
                    empty = textEmpty,
                    onValueChange = {
                        input = it
                    }
                )

                Spacer(modifier = Modifier.width(6.dp))

                FloatingActionButton(
                    modifier = Modifier.size(48.dp),
                    backgroundColor = Color(0xff00897B),
                    onClick = {
                        if (!textEmpty) {
                            onMessageChange(input.text)
                            input = TextFieldValue("")
                        }
                    }
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = if (textEmpty) Icons.Filled.Mic else Icons.Filled.Send,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ChatTextField(
    modifier: Modifier = Modifier,
    input: TextFieldValue,
    empty: Boolean,
    onValueChange: (TextFieldValue) -> Unit
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colors.surface,
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {

                IndicatingIconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.then(Modifier.size(circleButtonSize)),
                    indication = rememberRipple(bounded = false, radius = circleButtonSize / 2)
                ) {
                    Icon(imageVector = Icons.Default.Mood, contentDescription = "emoji")
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = circleButtonSize),
                    contentAlignment = Alignment.CenterStart
                ) {

                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        textStyle = TextStyle(
                            fontSize = 18.sp
                        ),
                        value = input,
                        onValueChange = onValueChange,
                        cursorBrush = SolidColor(Color(0xff00897B)),
                        decorationBox = { innerTextField ->
                            if (empty) {
                                Text("Message", fontSize = 18.sp)
                            }
                            innerTextField()
                        }
                    )
                }

                IndicatingIconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.then(Modifier.size(circleButtonSize)),
                    indication = rememberRipple(bounded = false, radius = circleButtonSize / 2)
                ) {
                    Icon(
                        modifier = Modifier.rotate(-45f),
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "attach"
                    )
                }
                AnimatedVisibility(visible = empty) {
                    IndicatingIconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.then(Modifier.size(circleButtonSize)),
                        indication = rememberRipple(bounded = false, radius = circleButtonSize / 2)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "camera"
                        )
                    }
                }
            }
        }
    }
}

val circleButtonSize = 44.dp