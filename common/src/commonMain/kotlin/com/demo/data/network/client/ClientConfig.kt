package com.demo.data.network.client

@kotlin.native.concurrent.ThreadLocal
object ClientConfig {
    var ipAddress = ""
    var port = ""
}
