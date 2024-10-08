import net.ssehub.customdocker.SSEDocker
import net.ssehub.customdocker.Delegates

def call(Closure closure) {
    def ssedocker = new SSEDocker(docker)
    Delegates.call(ssedocker, closure)
    ssedocker.execute()
}