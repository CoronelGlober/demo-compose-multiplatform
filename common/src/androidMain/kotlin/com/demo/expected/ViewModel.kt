package com.demo.expected

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import androidx.lifecycle.viewModelScope as viewModelScopeExt

actual open class ViewModel actual constructor() : ViewModel() {

    actual val viewModelScope: CoroutineScope = viewModelScopeExt

    public actual override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}