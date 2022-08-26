package com.demo.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Processors executes all the long running/intensive tasks,
 * should always run on a coroutine context different from the MainUI
 */
interface EffectsProcessor<Effect : Any, out Action : Any> {

    suspend fun processEffect(effect: Effect): Flow<Action>
}
