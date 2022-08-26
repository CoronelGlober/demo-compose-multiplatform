package com.demo.expected

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

actual fun getPlatformName(): String {
    return "Desktop!"
}