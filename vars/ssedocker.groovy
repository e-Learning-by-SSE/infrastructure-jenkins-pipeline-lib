import net.ssehub.customdocker.SSEDocker
import net.ssehub.customdocker.Delegates

def call(Closure closure) {
    def sseDocker = new SSEDocker(docker)
    Delegates.call(sseDocker, this, closure)
    sseDocker.execute()
}