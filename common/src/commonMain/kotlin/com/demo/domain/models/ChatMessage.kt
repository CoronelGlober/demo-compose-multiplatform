package com.demo.domain.models

sealed class ChatMessage {
    abstract val userColor: String
    abstract val message: String

    data class ServerMessage(override val userColor: String, override val message: String) : ChatMessage()
    data class MyMessage(override val userColor: String, override val message: String) : ChatMessage()
    data class OtherMessage(
        val userName: String,
        override val userColor: String,
        override val message: String
    ) : ChatMessage()
}