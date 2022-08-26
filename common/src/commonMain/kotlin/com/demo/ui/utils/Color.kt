package com.demo.ui.utils

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val SentMessageColor = Color(0xffE7FFDB)
val ReceivedMessageColor = Color.White
val ServerMessageColor = Color(0xffd4eaf4)

//val BackgroundColor = Color(0xffefe8df)
val BackgroundColor = Color(0xffFBE9E7)
fun Color.enlightenColor(factor: Float): Color {

    val colorFactor = factor.coerceAtLeast(0f)
    return copy(
        alpha = alpha * colorFactor
    )
}
