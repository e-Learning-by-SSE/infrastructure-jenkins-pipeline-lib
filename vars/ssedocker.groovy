import net.ssehub.customdocker.SSEDocker
import net.ssehub.customdocker.Delegates

def call(Closure closure) {
    def ssedocker = new SSEDocker(docker)
    closure.delegate = ssedocker
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure.call()
    ssedocker.execute()
}