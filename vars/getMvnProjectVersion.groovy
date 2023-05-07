def call() {
  // legacy support
  script {
    return maven.getVersion()
  }
}