package com.demo.data.dto

import com.demo.data.dto.SenderIdentity.Other
import kotlinx.serialization.Serializable

@Serializable
sealed class SenderIdentity(open val identity: String) {
    @Serializable
    object Server : SenderIdentity("Server")

    @Serializable
    object Me : SenderIdentity("me")

    @Serializable
    object Other : SenderIdentity("other")
}


@Serializable
data class UserDTO(
    val userName: String = "",
    val userColor: String = "",
    val identity: SenderIdentity
)

@Serializable
data class ChatMessageDTO(val sender: UserDTO = UserDTO(identity = Other), val content: String)