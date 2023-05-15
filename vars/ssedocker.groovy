import net.ssehub.customdocker.SSEDocker
import net.ssehub.customdocker.Delegates

def call(Closure closure) {
    node {
        def ssedocker = new SSEDocker(docker)
        Delegates.call(ssedocker, closure)
        ssedocker.execute()
    }
}