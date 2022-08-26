package com.demo.mvi

import com.demo.expected.ViewModel
import com.demo.expected.backgroundDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Principal element of this reactive MVI approach, handles the actions and effects dispatching
 * on their proper coroutine context, exposes the #State, #UiState and #Event as #Flow so they
 * can be listened/collected in a pure Kotlin coroutine fashion
 */
open class MviViewModel<Action : Any, State : Any, UiState : Any, Effect : Any, Event : Any>(
    private val stateUpdater: StateUpdater<State, Action, Effect, Event>,
    private val effectsProcessor: EffectsProcessor<Effect, Action>,
    private val stateMapper: StateMapper<State, UiState>,
    val initialState: State,
    initialEffects: Set<Effect> = emptySet()
) : ViewModel(), ActionDispatcher<Action>, EffectsDispatcher<Effect> {

    private val state = MutableStateFlow(initialState)
    private val events = MutableSharedFlow<Event>()

    fun getStateFlow(): Flow<State> = state.asStateFlow()
    fun getUiStateFlow(): StateFlow<UiState> {
        return state.map { stateMapper.mapToUiState(it) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, stateMapper.mapToUiState(initialState))
    }

    fun getEventsFlow(): Flow<Event> = events.asSharedFlow()

    init {
        if (initialEffects.isNotEmpty()) {
            dispatchEffects(initialEffects)
        }
    }

    final override fun dispatchAction(action: Action) {

        val nextState = stateUpdater.processNewAction(action, state.value)
        if (nextState.effects.isNotEmpty()) {
            dispatchEffects(nextState.effects)
        }
        if (nextState.events.isNotEmpty()) {
            viewModelScope.launch {
                nextState.events.forEach { event ->
                    events.emit(event)
                }
            }
        }
        viewModelScope.launch {
            state.emit(nextState.state)
        }
    }

    final override fun dispatchEffects(effects: Set<Effect>) {
        effects.forEach { effect ->
            viewModelScope.launch {
                withContext(backgroundDispatcher) {
                    effectsProcessor.processEffect(effect).collect { resultAction ->
                        withContext(viewModelScope.coroutineContext) {
                            dispatchAction(resultAction)
                        }
                    }
                }
            }
        }
    }
}
