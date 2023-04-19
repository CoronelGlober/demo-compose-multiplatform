package com.demo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.ui.zIndex
import com.demo.domain.screens.optionsscreen.MainScreenState
import com.demo.domain.screens.optionsscreen.MainScreenViewModel
import com.demo.expected.getPlatformName
import com.demo.ui.composables.MainScreen
import com.demo.ui.DesktopTheme
import com.demo.ui.themes.LocalCommonColors
import com.demo.ui.utils.chat.Close_face
import com.demo.ui.utils.chat.Floating_screen
import com.demo.ui.utils.chat.Max_Screen


val viewModel = MainScreenViewModel()

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    var isShowingTools by remember { mutableStateOf(false) }
    val state = rememberWindowState(placement = WindowPlacement.Floating)
    val iconPainter = rememberVectorPainter(Icons.Filled.Max_Screen)

    val trayState = rememberTrayState()
    Tray(state = trayState, icon = TrayIcon)

    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = getPlatformName(),
            transparent = true,
            undecorated = true, //transparent window must be undecorated
            state = state
        ) {
            if (isShowingTools) {
                DesktopMenu(mutableStateOf(isShowingTools), iconPainter, ::exitApplication)
            }

            DesktopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(5.dp).shadow(3.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp) //window has round corners now
                ) {
                    WindowDraggableArea {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                            MainScreen(viewModel) {
                                val newNotification = Notification("new message from ${it.userName}", it.message)
                                trayState.sendNotification(newNotification)
                            }
                            Row(
                                modifier = Modifier.zIndex(1f).padding(15.dp),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                TooltipArea(
                                    tooltip = {
                                        // composable tooltip content
                                        Surface(
                                            modifier = Modifier.shadow(4.dp),
                                            color = Color(255, 255, 210),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Close app!",
                                                modifier = Modifier.padding(10.dp),
                                                color = Color.Black
                                            )
                                        }
                                    },
                                    modifier = Modifier,
                                    delayMillis = 600, // in milliseconds
                                    tooltipPlacement = TooltipPlacement.CursorPoint(
                                        alignment = Alignment.BottomEnd,
                                        offset = DpOffset(-16.dp, 0.dp)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close_face,
                                        contentDescription = "",
                                        modifier = Modifier.size(25.dp)
                                            .clip(CircleShape)
                                            .background(LocalCommonColors.current.error)
                                            .clickable { exitApplication() },
                                        tint = Color.Black
                                    )
                                }
                                TooltipArea(
                                    tooltip = {
                                        // composable tooltip content
                                        Surface(
                                            modifier = Modifier.shadow(4.dp),
                                            color = Color(255, 255, 210),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Make window Float",
                                                modifier = Modifier.padding(10.dp),
                                                color = Color.Black
                                            )
                                        }
                                    },
                                    modifier = Modifier,
                                    delayMillis = 600, // in milliseconds
                                    tooltipPlacement = TooltipPlacement.CursorPoint(
                                        alignment = Alignment.BottomEnd,
                                        offset = DpOffset(-16.dp, 0.dp)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Floating_screen,
                                        contentDescription = "",
                                        modifier = Modifier.size(25.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFFF00))
                                            .clickable { state.placement = WindowPlacement.Floating },
                                        tint = Color.Black
                                    )
                                }
                                TooltipArea(
                                    tooltip = {
                                        // composable tooltip content
                                        Surface(
                                            modifier = Modifier.shadow(4.dp),
                                            color = Color(255, 255, 210),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Maximize",
                                                modifier = Modifier.padding(10.dp),
                                                color = Color.Black
                                            )
                                        }
                                    },
                                    modifier = Modifier,
                                    delayMillis = 600, // in milliseconds
                                    tooltipPlacement = TooltipPlacement.CursorPoint(
                                        alignment = Alignment.BottomEnd,
                                        offset = DpOffset(-16.dp, 0.dp)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Max_Screen,
                                        contentDescription = "",
                                        modifier = Modifier.size(25.dp)
                                            .clip(CircleShape)
                                            .zIndex(1f)
                                            .background(Color(0XFF00FF00))
                                            .clickable { state.placement = WindowPlacement.Maximized },
                                        tint = Color.Black
                                    )
                                }
                                val menuState = if (isShowingTools) "OFF" else "ON"
                                TooltipArea(
                                    tooltip = {
                                        // composable tooltip content
                                        Surface(
                                            modifier = Modifier.shadow(4.dp),
                                            color = Color(255, 255, 210),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Toggle title menu $menuState",
                                                modifier = Modifier.padding(10.dp),
                                                color = Color.Black
                                            )
                                        }
                                    },
                                    modifier = Modifier.padding(start = 15.dp),
                                    delayMillis = 600, // in milliseconds
                                    tooltipPlacement = TooltipPlacement.CursorPoint(
                                        alignment = Alignment.BottomEnd,
                                        offset = DpOffset(-16.dp, 0.dp)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "",
                                        modifier = Modifier.size(25.dp)
                                            .clip(CircleShape)
                                            .zIndex(1f)
                                            .background(Color(0XFF00FFFF))
                                            .clickable { isShowingTools = !isShowingTools },
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun FrameWindowScope.DesktopMenu(isShowingTools: MutableState<Boolean>, iconPainter: Painter, onClose: () -> Unit) {

    MenuBar {
        Menu("Go to", mnemonic = 'G') {
            CheckboxItem(
                "Show menu bar",
                checked = isShowingTools.value,
                onCheckedChange = {
                    isShowingTools.value = !isShowingTools.value
                }
            )
            Separator()
            Item(
                "Coroutines example",
                onClick = { viewModel.navigateToScreen(MainScreenState.SecondsCounter) })
            Item(
                "Drawing example",
                onClick = { viewModel.navigateToScreen(MainScreenState.SharedDrawingPad) })
            Item(
                "Chat/IO example",
                onClick = { viewModel.navigateToScreen(MainScreenState.SharedChatRoom) })
            Separator()
            Menu("Additional Settings") {
                Item("Setting 1", onClick = { })
                Item("Setting 2", onClick = { })
            }
            Separator()
            Item("About", icon = iconPainter, onClick = { })
            Item("Exit", onClick = onClose, shortcut = KeyShortcut(Key.Escape), mnemonic = 'E')
        }
    }
}