package com.demo.data.network.client

import com.demo.data.dto.ChatMessageDTO
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.isClosed
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.CancellationException
import io.ktor.utils.io.cancel
import io.ktor.utils.io.close
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TCPClient constructor(private val backgroundDispatcher: CoroutineDispatcher) {

    private val printingInConsole: Boolean = true
    val eventsStream = MutableStateFlow("")
    val newMessagesStream: MutableStateFlow<ChatMessageDTO?> = MutableStateFlow(null)
    val errorsStream: MutableStateFlow<String> = MutableStateFlow("")


    private var writingChannel: ByteWriteChannel? = null
    private var readingChannel: ByteReadChannel? = null
    private var currentSocketConnection: Socket? = null


    suspend fun connectToServer(ipAddress: String, port: Int) {
        log("launching coroutine")
        val selectorManager = SelectorManager(backgroundDispatcher)
        log("connecting socket")
        withContext(backgroundDispatcher) {
            currentSocketConnection = aSocket(selectorManager).tcp().connect(ipAddress, port)
            log("connected to server $ipAddress:$port")
            log("launching coroutines")

            prepareForSendingMessages()
        }
        log("coroutines dispatched")
    }

    private fun prepareForSendingMessages() {
        try {
            writingChannel = currentSocketConnection?.openWriteChannel(true)
            log(if (writingChannel != null) "ready to send messages" else "null writing channel")

        } catch (e: Exception) {
            log("Error while opening writing channel: " + e.message)
            errorsStream.value = "Error while opening writing channel:" + e.message.orEmpty()
        }
    }

    suspend fun sendMessage(message: ChatMessageDTO): Boolean {

        var messageSent = false
        try {
            writingChannel?.let { channel ->
                if (currentSocketConnection?.isClosed == false) {
                    log("sending input from user to the server")
                    val serializedMessage = Json.encodeToString(message)
                    withContext(backgroundDispatcher) {
                        log("sending input from user to the server: $serializedMessage")
                        channel.writeStringUtf8(serializedMessage + "\n")
                        log("message sent to the server")
                    }
                    messageSent = true
                } else {
                    log("couldn't send message, socket connection closed")
                }
            } ?: kotlin.run {
                log("couldn't send message, writing channel is null")
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                releaseConnection()
            }
            log("Error while sending: " + e.message)
            errorsStream.value = "Error sending new message:" + e.message.orEmpty()
        }
        return messageSent

    }

    suspend fun startListeningServerMessages() {
        withContext(backgroundDispatcher) {
            try {
                readingChannel = currentSocketConnection?.openReadChannel()
                while (currentSocketConnection?.isClosed == false) {
                    log("Waiting for message from server")
                    val message = readingChannel?.readUTF8Line().orEmpty()
                    if (message.isNotEmpty()) {
                        val messageParsed = try {
                            Json.decodeFromString(message)
                        } catch (e: Exception) {
                            ChatMessageDTO(content = message)
                        }
                        newMessagesStream.value = messageParsed
                        log("server said: $message")
                    } else {
                        log("server sent null or empty answer, probably server shut down, closing connection")
                        currentSocketConnection?.close()
                        log("connection closed")
                        break
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    releaseConnection()
                }
                log("Error while receiving: " + e.message)
                errorsStream.value = "Error receiving incoming message:" + e.message.orEmpty()
            }
        }
    }

    private fun releaseConnection() {
        try {
            writingChannel?.close()
            readingChannel?.cancel()
            currentSocketConnection?.close()
        } catch (ex: Exception) {
            log("error while releasing connection: " + ex.message)
        }
    }

    private fun log(message: String) {
        if (printingInConsole) {
            println(message)
        }
        eventsStream.value = message
    }
}