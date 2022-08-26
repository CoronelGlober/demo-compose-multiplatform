package com.demo.expected

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val backgroundDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default