def call(updateCommand) {
  // deprecated - legacy support
  println("the method stagingDeploy is deprecated. It was renamed to staging01ssh")
  staging01ssh(updateCommand)
}