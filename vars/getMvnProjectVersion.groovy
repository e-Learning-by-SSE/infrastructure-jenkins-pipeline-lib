def call() {
  println("DEPRECATION NOTICE: please use maven.getProjectVersion() in the future")
  script {
    return maven.getProjectVersion()
  }
}