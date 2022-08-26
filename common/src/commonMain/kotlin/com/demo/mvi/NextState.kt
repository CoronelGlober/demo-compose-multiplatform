package com.demo.mvi

/**
 * Represents the mutated state with the effects and events the change produced
 */
data class NextState<State : Any, Effect : Any, Event : Any>(
    val state: State,
    val effects: Set<Effect> = setOf(),
    val events: Set<Event> = setOf()
)
