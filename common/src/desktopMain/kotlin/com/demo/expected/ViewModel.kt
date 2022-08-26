package com.demo.expected

import com.demo.mvi.createViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

actual open class ViewModel actual constructor() {

    actual val viewModelScope: CoroutineScope = createViewModelScope

    actual open fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}