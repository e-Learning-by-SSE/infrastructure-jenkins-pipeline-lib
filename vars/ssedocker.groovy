def call(Closure closure) {
    def sseDocker = new SSEDocker()
    Delegates.call(sseDocker, closure)
    sseDocker.execute()
}