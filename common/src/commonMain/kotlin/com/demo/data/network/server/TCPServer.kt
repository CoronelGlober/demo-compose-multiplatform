package com.demo.data.network.server

import com.demo.data.dto.ChatMessageDTO
import com.demo.data.dto.SenderIdentity
import com.demo.data.dto.SenderIdentity.Server
import com.demo.data.dto.UserDTO
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Connection(val user: UserDTO, val socket: Socket, val sendChannel: ByteWriteChannel)

class TCPServer {
    private val printingInConsole = true
    private val connections = LinkedHashMap<Int, Connection>()
    private val events = MutableStateFlow("")
    val eventsStream: Flow<String> = events

    fun startServer(ipAddress: String, port: Int) {
        runBlocking {
            startServerAsync(ipAddress, port)
        }
    }

    // for killing app running on port 8080 -> kill -9 $(lsof -ti:8080)
    private suspend fun startServerAsync(ipAddress: String, port: Int) {
        log("server - Starting server")
        withContext(Dispatchers.Default) {
            val selectorManager = SelectorManager(Dispatchers.Default)
            log("server - creating server")
            try {
                val server = aSocket(selectorManager).tcp().bind(ipAddress, port)
                log("server - server running at $ipAddress:$port")
                while (true) {
                    log("server - waiting new connection")
                    val socket = server.accept()
                    log("server - connection received, launching coroutine for handling connection")
                    launch { handleConnection(socket) }
                }
            } catch (ex: Exception) {
                println("Error while running server")
                ex.printStack()
            }
        }
    }

    private suspend fun handleConnection(socket: Socket) {
        val sendChannel = socket.openWriteChannel(autoFlush = true)
        log("coroutine - sending greeting")
        sendChannel.writeStringUtf8(serializeMessage(getServerMessageDTO("Hi new user, what is your name?")) + "\n")
        log("coroutine - greeting sent")
        val receiveChannel = socket.openReadChannel()
        var addedUserHash = 0
        try {
            var message = receiveChannel.readUTF8Line().orEmpty()
            val greeting = Json.decodeFromString<ChatMessageDTO>(message)
            log("Connected new user - " + greeting.content)
            val newUser = UserDTO(userName = greeting.content, userColor = generateRandomColor(), identity = SenderIdentity.Other)
            val connection = Connection(user = newUser, socket = socket, sendChannel = sendChannel)
            addedUserHash = connection.hashCode()
            log("adding new hash -> $addedUserHash")
            connections[addedUserHash] = connection
            sendChannel.writeStringUtf8(serializeMessage(getWelcomeMessageDTO(newUser)) + "\n")
            broadcastMessage(
                senderHash = addedUserHash,
                forAllUsers = false,
                message = getServerMessageDTO("'${newUser.userName}' connected")
            )
            while (!socket.isClosed) {
                log("coroutine - waiting new message from client")
                message = receiveChannel.readUTF8Line().orEmpty()
                if (message != "exit!" && message.trim().isNotEmpty()) {
                    log("coroutine - received new message from: ${newUser.userName}")
                    val parsedMessage = Json.decodeFromString<ChatMessageDTO>(message).copy(sender = newUser)
                    broadcastMessage(senderHash = addedUserHash, forAllUsers = true, message = parsedMessage)
                } else {
                    log("coroutine - connection exited")
                    socket.close()
                    log("coroutine - socket closed")
                    break
                }
            }
        } catch (ex: Exception) {
            log("coroutine - error occurred, maybe client disconnected...")
            socket.close()
        }
        var addedUserName = "unknown user"
        if (addedUserHash != 0) {
            addedUserName = connections[addedUserHash]?.user?.userName ?: "unknown user"
        }
        if (addedUserHash != 0) {
            log("coroutine - closing connection from $addedUserName")
            connections.remove(addedUserHash)
            broadcastMessage(
                senderHash = addedUserHash,
                forAllUsers = true,
                message = getServerMessageDTO("'${addedUserName}' disconnected")
            )
        }
        log("coroutine - socket closed, completing coroutine handling connection with $addedUserName")
    }

    private fun getServerMessageDTO(message: String): ChatMessageDTO {
        return ChatMessageDTO(UserDTO("Server", "ffd4eaf4", identity = Server), message)
    }

    private fun getWelcomeMessageDTO(user: UserDTO): ChatMessageDTO {
        return ChatMessageDTO(UserDTO("Server", user.userColor, identity = Server), "Welcome ${user.userName}!")
    }

    private suspend fun broadcastMessage(senderHash: Int, forAllUsers: Boolean, message: ChatMessageDTO) {
        connections
            .filter { forAllUsers || it.key != senderHash }
            .forEach {
                val senderIdentity = when {
                    message.sender.identity is Server -> Server
                    it.key == senderHash -> SenderIdentity.Me
                    else -> SenderIdentity.Other
                }
                val sentMessage =
                    serializeMessage(message.copy(sender = message.sender.copy(identity = senderIdentity)))
                log("coroutine - sending message from: '${message.sender.userName}' to: '${it.value.user.userName}'")
                it.value.sendChannel.writeStringUtf8(sentMessage + "\n")
            }
    }

    private fun serializeMessage(message: ChatMessageDTO): String {
        return Json.encodeToString(message)
    }

    private fun log(message: String) {
        if (printingInConsole) {
            println(message)
        }
        events.value = message
    }

    private fun generateRandomColor(): String {
        val index = connections.size % (colors.size + 1)
        return colors[index]
    }

    private val colors = listOf("FF1789FC","FFCC2936","FFFCEF55","FF2C5F2D","FF3D79FF")
}