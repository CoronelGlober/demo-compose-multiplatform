import com.demo.data.network.server.TCPServer

/**
 * Console tcp relay server that handles connections via coroutines,
 * broadcasts each message received to all the identified clients
 *
 * Following tasks are available on the gradle panel:
 *
 * running it from gradle tasks panel:
 *      -> server/Tasks/application/run
 *
 * make binary redistributable -> server/Tasks/distribution/assembleDist
 *      -> this creates a .zip file at /build/distributions, there you will find the executable server
 *
 * for killing a server running on port 8080 run this command in console -> kill -9 $(lsof -ti:8080)
 */
fun main() {
    println("Enter IP Address for starting server")
    val ipAddress = readlnOrNull()
    println("Enter port for starting server")
    val port = readlnOrNull()
    TCPServer().startServer(ipAddress!!, port!!.toInt())
}
