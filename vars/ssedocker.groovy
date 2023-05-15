import net.ssehub.customdocker.SSEDocker
import net.ssehub.customdocker.Delegates

def call(Closure closure) {
    script {
        def ssedocker = new SSEDocker(docker)
        Delegates.call(ssedocker, closure)
        ssedocker.execute()
    }
}