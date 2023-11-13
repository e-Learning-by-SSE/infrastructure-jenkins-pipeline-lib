import java.net.Socket
import java.util.Random

def generateAvailablePort(int startPort = 3000, int endPort = 9000) {
    int port
    while (true) {
        port = new Random().nextInt(endPort - startPort + 1) + startPort // Random number within the specified range
        if (isPortAvailable(port)) {
            break
        }
    }
    return port
}

def isPortAvailable(int port) {
    Socket socket = null
    try {
        socket = new Socket("localhost", port)
        return false // Port is in use
    } catch (Exception e) {
        return true // Port is available
    } finally {
        if (socket != null && !socket.isClosed()) {
            socket.close()
        }
    }
}
