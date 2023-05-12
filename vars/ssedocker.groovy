def ssedocker(Closure closure) {
    def sseDocker = new SSEDocker()
    Delegates.call(sseDocker, closure)
    sseDocker.execute()
}