def call(String cmd) {
    return sh(script: "${cmd}", returnStdout: true).trim()
}