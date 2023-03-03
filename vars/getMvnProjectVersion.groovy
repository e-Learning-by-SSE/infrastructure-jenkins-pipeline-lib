def call() {
  return sh(
      returnStdout: true,
      script: 'mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout')
    .trim()
}