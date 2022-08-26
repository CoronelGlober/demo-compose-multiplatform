package com.demo.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.data.network.client.ClientConfig
import com.demo.domain.models.ChatMessage
import com.demo.domain.screens.client.mvi.ClientActions
import com.demo.domain.screens.client.mvi.ClientActions.SendMessage
import com.demo.domain.screens.client.mvi.ClientEvents.ErrorHappened
import com.demo.domain.screens.client.mvi.ClientEvents.MessageReceived
import com.demo.domain.screens.client.mvi.ClientScreenState
import com.demo.domain.screens.client.mvi.ClientViewModel
import com.demo.expected.platformBottomSpacing
import com.demo.ui.utils.BackgroundColor
import com.demo.ui.utils.ReceivedMessageColor
import com.demo.ui.utils.SentMessageColor
import com.demo.ui.utils.ServerMessageColor
import com.demo.ui.utils.chat.*
import com.smarttoolfactory.speechbubble.Padding
import kotlinx.coroutines.launch

@Composable
fun ChatClient(
    viewModel: ClientViewModel = ClientViewModel(),
    messageReceived: (ChatMessage.OtherMessage) -> Unit
) {
    ClientContent(viewModel, messageReceived)
}

@Composable
private fun ClientContent(
    viewModel: ClientViewModel,
    messageReceived: (ChatMessage.OtherMessage) -> Unit
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var messages by remember { mutableStateOf((listOf<ChatMessage>())) }
    val state = viewModel.getUiStateFlow().collectAsState()

    LaunchedEffect(true) {
        viewModel.dispatchAction(ClientActions.Connect(ClientConfig.ipAddress, ClientConfig.port.toInt()))
        viewModel.getEventsFlow().collect { newEvent ->
            when (newEvent) {
                is ErrorHappened -> println(newEvent.error)
                is MessageReceived -> {
                    messages = messages + newEvent.message
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(messages.size - 1)
                    }
                    if (newEvent.message is ChatMessage.OtherMessage) {
                        messageReceived(newEvent.message)
                    }
                }
                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor).padding(bottom = platformBottomSpacing)
    ) {
        val messagesModifier = Modifier
            .fillMaxWidth()
            .weight(1f)

        LazyColumn(modifier = messagesModifier, state = scrollState) {
            items(messages) { message ->
                when (message) {
                    is ChatMessage.ServerMessage -> ServerMessage(message)
                    is ChatMessage.MyMessage -> MyMessage(message)
                    is ChatMessage.OtherMessage -> OtherMessage(message)
                }
            }
        }
        if (state.value is ClientScreenState.Connected) {
            ChatInput(
                onMessageChange = { messageContent ->
                    viewModel.dispatchAction(SendMessage(messageContent))
                }
            )
        }
    }
}

@Composable
fun ServerMessage(message: ChatMessage) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BubbleLayout(
            bubbleState = rememberBubbleState(
                alignment = ArrowAlignment.None,
                backgroundColor = ServerMessageColor,
                cornerRadius = 5.dp,
                shadow = BubbleShadow(
                    elevation = 1.dp
                ),
                padding = Padding(8.dp)
            )

        ) {
            Text(
                message.message,
                color = Color.Black,
                fontSize = 15.sp,
            )
        }
    }
}

@Composable
fun MyMessage(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.End
    ) {
        BubbleLayout(
            bubbleState = rememberBubbleState(
                backgroundColor = SentMessageColor,
                alignment = ArrowAlignment.RightTop,
                arrowWidth = 20.dp,
                arrowHeight = 20.dp,
                cornerRadius = 8.dp,
                shadow = BubbleShadow(elevation = 1.dp),
                padding = Padding(8.dp)
            )
        ) {
            Text(
                text = message.message,
                color = Color.Black,
                fontSize = 15.sp,
            )
        }
    }
}

@Composable
fun OtherMessage(message: ChatMessage.OtherMessage) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        BubbleLayout(
            bubbleState = rememberBubbleState(
                backgroundColor = ReceivedMessageColor,
                alignment = ArrowAlignment.LeftTop,
                arrowWidth = 20.dp,
                arrowHeight = 20.dp,
                cornerRadius = 8.dp,
                shadow = BubbleShadow(elevation = 1.dp),
                padding = Padding(8.dp)
            )
        ) {
            Text(
                text = message.userName,
                color = Color(message.userColor.toLong(16)),
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )
            Text(
                text = message.message,
                color = Color.Black,
                fontSize = 15.sp,
            )
        }
    }
}