package com.demo.domain.screens.client.mvi

import com.demo.data.dto.ChatMessageDTO
import com.demo.data.dto.SenderIdentity.*
import com.demo.data.network.client.TCPClient
import com.demo.domain.models.ChatMessage
import com.demo.domain.models.ChatMessage.*
import com.demo.mvi.EffectsProcessor
import com.demo.mvi.NextState
import com.demo.mvi.StateMapper
import com.demo.mvi.StateUpdater
import kotlinx.coroutines.flow.*

sealed class ClientScreenState {
    object Disconnected : ClientScreenState()
    object Connecting : ClientScreenState()
    data class Connected(val userName: String = "") : ClientScreenState()
}

sealed class ClientEffects {
    data class Connect(val ipAddress: String, val port: Int) : ClientEffects()
    data class SendMessage(val message: ChatMessageDTO) : ClientEffects()
    object StartListeningForMessages : ClientEffects()
}

sealed class ClientActions {
    data class Connect(val ipAddress: String, val port: Int) : ClientActions()
    object Connected : ClientActions()
    data class SendMessage(val message: String) : ClientActions()
    data class MessageReceived(val message: ChatMessage) : ClientActions()
    data class ErrorHappened(val error: String) : ClientActions()
    data class LogEventReceived(val loggedEvent: String) : ClientActions()
}

sealed class ClientEvents {
    data class MessageReceived(val message: ChatMessage) : ClientEvents()
    data class ConnectionChanged(val connected: Boolean) : ClientEvents()
    data class ErrorHappened(val error: String) : ClientEvents()
    data class LogEventReceived(val loggedEvent: String) : ClientEvents()
}

class ClientProcessor constructor(private val tcpClient: TCPClient, private val sendBackMyMessages: Boolean) :
    EffectsProcessor<ClientEffects, ClientActions> {
    override suspend fun processEffect(effect: ClientEffects): Flow<ClientActions> {
        return when (effect) {
            is ClientEffects.Connect -> connectClient(effect)
            is ClientEffects.SendMessage -> sendMessage(effect)
            is ClientEffects.StartListeningForMessages -> startListening()
        }
    }

    private fun startListening(): Flow<ClientActions> {
        val messages = tcpClient.newMessagesStream.filterNotNull().map { messageDto ->
            val message = with(messageDto) {
                when (sender.identity) {
                    Server -> ServerMessage(sender.userColor, content)
                    Me -> MyMessage(sender.userColor, content)
                    Other -> OtherMessage(sender.userName, sender.userColor, content)
                }
            }
            ClientActions.MessageReceived(message)
        }.filter { sendBackMyMessages || it.message !is MyMessage } // filtering only my messages
        val errors = tcpClient.errorsStream.mapNotNull { error ->
            ClientActions.ErrorHappened(error)
        }
        val logs = tcpClient.eventsStream.mapNotNull { loggedEvent ->
            ClientActions.LogEventReceived(loggedEvent)
        }
        return merge(logs, messages, errors)
    }

    private suspend fun sendMessage(effect: ClientEffects.SendMessage): Flow<ClientActions> = flow {
        tcpClient.sendMessage(effect.message)
    }

    private suspend fun connectClient(effect: ClientEffects.Connect): Flow<ClientActions> = flow {
        try {
            tcpClient.connectToServer(effect.ipAddress, effect.port)
            emit(ClientActions.Connected)
            tcpClient.startListeningServerMessages()
        } catch (ex: Exception) {
            emit(ClientActions.ErrorHappened("error establishing connection: " + ex.message))
        }
    }
}

class ClientUpdater : StateUpdater<ClientScreenState, ClientActions, ClientEffects, ClientEvents> {
    override fun processNewAction(
        action: ClientActions,
        currentState: ClientScreenState
    ): NextState<ClientScreenState, ClientEffects, ClientEvents> {
        return when (action) {
            is ClientActions.Connect -> NextState(
                ClientScreenState.Connecting,
                setOf(ClientEffects.Connect(action.ipAddress, action.port), ClientEffects.StartListeningForMessages)
            )
            ClientActions.Connected -> NextState(
                ClientScreenState.Connected(),
                events = setOf(ClientEvents.ConnectionChanged(true))
            )
            is ClientActions.ErrorHappened -> NextState(
                currentState,
                events = setOf(ClientEvents.ErrorHappened(action.error))
            )
            is ClientActions.LogEventReceived -> NextState(
                currentState,
                events = setOf(ClientEvents.LogEventReceived(action.loggedEvent))
            )
            is ClientActions.MessageReceived -> NextState(
                ClientScreenState.Connected(),
                events = setOf(ClientEvents.MessageReceived(action.message))
            )
            is ClientActions.SendMessage -> sendMessage(currentState, action)
        }
    }

    private fun sendMessage(
        currentState: ClientScreenState,
        action: ClientActions.SendMessage
    ): NextState<ClientScreenState, ClientEffects, ClientEvents> {
        var state = currentState
        val effects = mutableSetOf<ClientEffects>()
        val events = mutableSetOf<ClientEvents>()
        if (currentState is ClientScreenState.Connected) {
            if (currentState.userName.isEmpty()) {
                state = currentState.copy(userName = action.message)
            }
            val message = ChatMessageDTO(content = action.message)
            effects.add(ClientEffects.SendMessage(message))
        } else {
            events.add(ClientEvents.ErrorHappened("Couldn't send message, connection is closed"))
        }
        return NextState(state, effects, events)
    }

}

class ClientStateMapper : StateMapper<ClientScreenState, ClientScreenState> {
    override fun mapToUiState(state: ClientScreenState): ClientScreenState {
        return state
    }
}