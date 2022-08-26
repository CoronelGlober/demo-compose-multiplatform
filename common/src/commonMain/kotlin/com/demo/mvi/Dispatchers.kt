package com.demo.mvi

/**
 * Dispatchers are responsible for orchestrating the actions and the effects,
 * ideally they will be implemented by a {ViewModel} that take care of
 * executing them in the proper coroutine context
 */
interface ActionDispatcher<Action : Any> {
    fun dispatchAction(action: Action)
}

interface EffectsDispatcher<Effect : Any> {
    fun dispatchEffects(effects: Set<Effect>)
}
