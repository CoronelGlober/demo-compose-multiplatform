package com.demo.expected

import com.demo.mvi.createViewModelScope
import kotlin.native.internal.GC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual open class ViewModel actual constructor() {
    actual val viewModelScope: CoroutineScope = createViewModelScope

    actual open fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        dispatch_async(dispatch_get_main_queue()) { GC.collect() }
    }
}