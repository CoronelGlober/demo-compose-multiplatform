package com.demo.mvi

/**
 * Performs all the business logic executions, it is the only responsible of mutating the #State
 * and requesting the dispatch of #Effects and #Events
 */
interface StateUpdater<State : Any, Action : Any, Effect : Any, Event : Any> {
    fun processNewAction(action: Action, currentState: State): NextState<State, Effect, Event>
}
