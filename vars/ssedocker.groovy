import customDocker.SSEDocker

def call(Closure closure) {
    def sseDocker = new SSEDocker(docker)
    Delegates.call(sseDocker, closure)
    sseDocker.execute()
}