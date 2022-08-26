package com.demo.expected

import kotlinx.coroutines.CoroutineScope

expect open class ViewModel() {
    val viewModelScope: CoroutineScope

    open fun onCleared()
}