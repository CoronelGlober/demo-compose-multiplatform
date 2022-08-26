package com.demo.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.demo.data.network.client.ClientConfig
import com.demo.domain.models.ChatMessage
import com.demo.domain.screens.client.mvi.ClientViewModel
import com.demo.domain.screens.counter.SecondsCounterViewModel
import com.demo.domain.screens.optionsscreen.MainScreenState
import com.demo.domain.screens.optionsscreen.MainScreenState.SharedChatRoom
import com.demo.domain.screens.optionsscreen.MainScreenViewModel
import com.demo.expected.PlatformButton
import com.demo.expected.getPlatformBackArrow
import com.demo.expected.getPlatformChatToolbarColor
import com.demo.ui.themes.CommonTheme
import com.demo.ui.themes.LocalCommonColors

typealias MessageReceivedCallback = ((ChatMessage.OtherMessage) -> Unit)

/**
 * Initial options screen, showing the three navigation actions
 * - Basic multiplatform coroutines example
 * - Shared drawing canvas
 * - Chat example
 */
@Composable
fun MainScreen(vm: MainScreenViewModel = MainScreenViewModel(), messageReceived: MessageReceivedCallback? = null) {

    val screenState by vm.flowableState.collectAsState()
    CommonTheme {
        val topBarColor by animateColorAsState(
            if (screenState is SharedChatRoom) getPlatformChatToolbarColor() else LocalCommonColors.current.primary,
            animationSpec = tween(500, easing = FastOutLinearInEasing)
        )
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(backgroundColor = topBarColor) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (screenState !is MainScreenState.OptionsScreen) {
                        BackTopBarArrow(vm) // this draws the ios/Android back arrow style - looking like swiftUI X)
                    }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val title = when (screenState) {
                            is MainScreenState.OptionsScreen -> "Compose Multi Platform"
                            MainScreenState.SecondsCounter -> "Native coroutines example"
                            SharedChatRoom -> "Native Chat/IO example"
                            MainScreenState.SharedDrawingPad -> "Native drawing/IO example"
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }) {
            when (screenState) {
                is MainScreenState.OptionsScreen -> OptionsScreen(vm)
                MainScreenState.SecondsCounter -> SecondsCounter(SecondsCounterViewModel().also {
                    vm.setCurrentClearListener(
                        it::onCleared
                    )
                })
                SharedChatRoom -> ChatClient(
                    viewModel = ClientViewModel().also { vm.setCurrentClearListener(it::onCleared) }
                ) { newMessage ->
                    messageReceived?.invoke(newMessage)
                }
                MainScreenState.SharedDrawingPad -> DrawingPad(ClientViewModel(false).also {
                    vm.setCurrentClearListener(
                        it::onCleared
                    )
                })
            }
        }
    }
}

@Composable
fun BackTopBarArrow(vm: MainScreenViewModel) {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        IconButton(
            modifier = Modifier.zIndex(1f),
            onClick = { vm.navigateToScreen(MainScreenState.OptionsScreen) }) {
            getPlatformBackArrow()
        }
    }
}

@Composable
fun OptionsScreen(vm: MainScreenViewModel) {
    // pending action used for intercept screen navigation if not server is configured,
    // so if the user wants to go to the chat example and the server is not defined yet,
    // the navigation action becomes a pending action that will be executed after the server is configured
    val pendingAction by vm.pendingAction.collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlatformButton(enabled = true, onClick = { vm.navigateToScreen(MainScreenState.SecondsCounter) }) {
            Text("Native coroutines example")
        }
        PlatformButton(enabled = true, onClick = { vm.navigateToScreen(MainScreenState.SharedDrawingPad) }) {
            Text("Native Drawing/IO example")
        }
        PlatformButton(enabled = true, onClick = { vm.navigateToScreen(SharedChatRoom) }) {
            Text("Native Chat/IO example")
        }
    }
    pendingAction?.let { ConfigurationDialog(it) { vm.dismissPendingAction() } }
}

/**
 * Since the chat example and the drawing example connects to the local tcp server, we need a way of defining its ip and port,
 * this dialog will be shown only once for configuration purposes
 */
@Composable
fun ConfigurationDialog(action: () -> Unit, dismissAction: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    var ipAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                color = LocalCommonColors.current.background
            )
            .clickable(
                onClick = {
                    dismissAction()
                },
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .background(LocalCommonColors.current.secondaryVariant, RoundedCornerShape(10.dp))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                placeholder = { Text("Server address") },
                value = ipAddress,
                onValueChange = { ipAddress = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            TextField(
                placeholder = { Text("Server port") },
                value = port,
                onValueChange = { port = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            PlatformButton(
                enabled = isValidIPAddress(ipAddress) && port.toIntOrNull() != null,
                onClick = {
                    ClientConfig.ipAddress = ipAddress
                    ClientConfig.port = port
                    action()
                    dismissAction()
                }) {
                Text("Save server address")
            }
        }
    }
}

fun isValidIPAddress(ip: String): Boolean {
    val reg0To255 = ("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|\$)){4}\\b").toRegex()
    return reg0To255.matches(ip)
}