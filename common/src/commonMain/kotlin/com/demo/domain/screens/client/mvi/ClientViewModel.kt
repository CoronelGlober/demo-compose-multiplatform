package com.demo.domain.screens.client.mvi

import com.demo.data.network.client.TCPClient
import com.demo.expected.backgroundDispatcher
import com.demo.mvi.MviViewModel

/**
 * ViewModel handling all the TCP connection related actions, this will be used for both chat and shared drawing screens
 */
class ClientViewModel(sendBackMyMessages:Boolean = true) : MviViewModel<ClientActions, ClientScreenState, ClientScreenState, ClientEffects, ClientEvents>(
    stateUpdater = ClientUpdater(),
    effectsProcessor = ClientProcessor(TCPClient(backgroundDispatcher), sendBackMyMessages),
    stateMapper = ClientStateMapper(),
    initialState = ClientScreenState.Disconnected,
    initialEffects = setOf()
)